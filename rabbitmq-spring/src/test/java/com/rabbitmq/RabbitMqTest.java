package com.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.domain.Order;
import com.rabbitmq.domain.Packaged;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @Description 测试类
 * @Author FengJie
 * @Date 2020/11/13 17:35
 * @Version 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitMqTest {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 测试RabbitAdmin类
    @Test
    public void testRabbitAdmin() {

        // 测试创建交换机
        rabbitAdmin.declareExchange(new DirectExchange("test.direct.exchange", false, false));
        rabbitAdmin.declareExchange(new TopicExchange("test.topic.exchange", false, false));
        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout.exchange", false, false));

        // 测试创建队列
        rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));

        // 测试绑定交换机和队列
        rabbitAdmin.declareBinding(new Binding("test.direct.queue",
                Binding.DestinationType.QUEUE,
                "test.direct.exchange", "direct.#", new HashMap<>()));

        rabbitAdmin.declareBinding(BindingBuilder
                .bind(new Queue("test.topic.queue", false))
                .to(new TopicExchange("test.topic.exchange", false, false))
                .with("topic.#"));

        rabbitAdmin.declareBinding(BindingBuilder
                .bind(new Queue("test.fanout.queue", false))
                .to(new FanoutExchange("test.fanout.exchange", false, false)));

        // 测试清空队列
        rabbitAdmin.purgeQueue("test.topic.queue", false);

    }

    /* 消息模板组件RabbitTemplate测试 */
    // 发送消息测试
    @Test
    public void sendMessage01Test() {

        // 设置消息属性
        MessageProperties properties = new MessageProperties();
        properties.getHeaders().put("technical", "Java");
        properties.getHeaders().put("middleware", "RabbitMQ");

        // 设置消息
        String msg = "01.RabbitMQ使用RabbitTemplate方式发送消息。";
        Message message = new Message(msg.getBytes(), properties);

        // 发送消息
        rabbitTemplate.convertAndSend("spring.exchange01", "spring.save", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("---------------添加额外的设置--------------");
                message.getMessageProperties().getHeaders().put("technical", "Java + Python");
                message.getMessageProperties().getHeaders().put("distributed", "Dubbo");
                return message;
            }
        });

    }

    // 发送消息测试
    @Test
    public void sendMessage02Test() {

        // 设置消息属性
        MessageProperties properties = new MessageProperties();
        properties.setContentType("text/plain");
        properties.setContentEncoding("UTF-8");

        // 设置消息
        String msg = "02.RabbitMQ使用RabbitTemplate方式发送消息。";
        Message message = new Message(msg.getBytes(), properties);

        // 发送消息
        rabbitTemplate.send("spring.exchange01", "mq.save", message);
        rabbitTemplate.send("spring.exchange02", "rabbit.save", message);

    }

    // 发送消息测试
    @Test
    public void sendMessage03Test() {

        // 发送消息
        rabbitTemplate.convertAndSend("spring.exchange02",
                "rabbit.save", "03.RabbitMQ使用RabbitTemplate方式发送消息。");

    }

    // 发送Json格式数据转换测试
    @Test
    public void testSendJsonMessage() throws Exception {

        Order order = new Order();
        order.setId("1");
        order.setName("手机");
        order.setContent("华为Mete30 Pro旗舰版");

        // 将订单消息转换为json格式
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("Order for Json: " + json);

        // 设置消息属性
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json"); // contentType必须修改为application/json
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("spring.exchange01", "spring.order", message);

    }

    // 发送Java格式数据转换测试
    @Test
    public void testSendJavaMessage() throws Exception {

        Order order = new Order();
        order.setId("1");
        order.setName("电脑");
        order.setContent("华为电脑 Win10 旗舰版 64位 16G&1T");

        // 将订单消息转换为json格式
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("Order for Json: " + json);

        // 设置消息属性
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json"); // contentType必须修改为application/json
        messageProperties.getHeaders().put("__TypeId__", "com.rabbitmq.domain.Order");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("spring.exchange01", "spring.order", message);
    }

    // 发送Java多对象映射转换测试
    @Test
    public void testSendMappingMessage() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        Order order = new Order();
        order.setId("1");
        order.setName("电脑");
        order.setContent("华为电脑 Win10 旗舰版 64位 16G&1T");

        String jsonOrder = mapper.writeValueAsString(order);
        System.err.println("Order for Json: " + jsonOrder);

        // 设置消息属性
        MessageProperties messagePropertiesOrder = new MessageProperties();
        messagePropertiesOrder.setContentType("application/json"); // contentType必须修改为application/json
        messagePropertiesOrder.getHeaders().put("__TypeId__", "order");
        Message messageOrder = new Message(jsonOrder.getBytes(), messagePropertiesOrder);
        rabbitTemplate.send("spring.exchange01", "spring.order", messageOrder);

        Packaged pack = new Packaged();
        pack.setId("1");
        pack.setName("快递包裹");
        pack.setDescription("姓名:张三；地址:北京；电话:18010101101");

        String jsonPackaged = mapper.writeValueAsString(pack);
        System.err.println("Packaged for Json: " + jsonPackaged);

        // 设置消息属性
        MessageProperties messagePropertiesPackaged = new MessageProperties();
        messagePropertiesPackaged.setContentType("application/json"); // contentType必须修改为application/json
        messagePropertiesPackaged.getHeaders().put("__TypeId__", "packaged");
        Message messagePackaged = new Message(jsonPackaged.getBytes(), messagePropertiesPackaged);
        rabbitTemplate.send("spring.exchange01", "spring.pack", messagePackaged);

    }

    // 多类型转换器测试
    @Test
    public void testSendExtConverterMessage() throws Exception {

        // 设置图片格式消息
        byte[] body = Files.readAllBytes(Paths.get("E:\\document\\image\\", "bird.jpg"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("image/jpg");
        messageProperties.getHeaders().put("extName", "jpg");
        Message message = new Message(body, messageProperties);

        // 发送消息
        rabbitTemplate.send("", "spring.queue.image", message);

        //// 设置PDF文件消息
        //byte[] body = Files.readAllBytes(Paths.get("E:\\document\\file\\", "Test.pdf"));
        //MessageProperties messageProperties = new MessageProperties();
        //messageProperties.setContentType("application/pdf");
        //Message message = new Message(body, messageProperties);
        //
        // 发送消息
        //rabbitTemplate.send("", "spring.queue.pdf", message);

    }

}
