package com.rabbitmq;

import com.rabbitmq.adapter.MessageDelegate;
import com.rabbitmq.converter.ImageMessageConverter;
import com.rabbitmq.converter.PdfMessageConverter;
import com.rabbitmq.converter.TextMessageConverter;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * @Description 配置类
 * @Author FengJie
 * @Date 2020/11/13 17:26
 * @Version 1.0.0
 */
@Configuration
@ComponentScan({"com.rabbitmq.*"})
public class RabbitMqConfig {

    /**
     * 将ConnectionFactory连接工厂注入Spring容器
     *
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        return connectionFactory;

    }

    /**
     * 将RabbitAdmin类注入Spring容器
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {

        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);

        return rabbitAdmin;

    }

    /**
     * 针对消费者的配置
     * 1.设置交换机类型
     * 2.生声明队列
     * 3.将交换机和队列及逆行绑定
     */
    /* 第一个队列和第一个交换机绑定设置 */
    @Bean
    public TopicExchange exchange01() {
        return new TopicExchange("spring.exchange01", true, false);
    }
    @Bean
    public Queue queue01() {
        return new Queue("spring.queue01", true);
    }
    @Bean
    public Binding binding01() {
        return BindingBuilder.bind(queue01()).to(exchange01()).with("spring.*");
    }

    /* 第二个队列和第二个交换机绑定设置 */
    @Bean
    public TopicExchange exchange02() {
        return new TopicExchange("spring.exchange02", true, false);
    }
    @Bean
    public Queue queue02() {
        return new Queue("spring.queue02", true);
    }
    @Bean
    public Binding binding02() {
        return BindingBuilder.bind(queue02()).to(exchange02()).with("rabbit.*");
    }

    /* 第三个队列和第一个交换机绑定设置 */
    @Bean
    public Queue queue03() {
        return new Queue("spring.queue03", true);
    }
    @Bean
    public Binding binding03() {
        return BindingBuilder.bind(queue03()).to(exchange01()).with("mq.*");
    }

    /* 队列持久 */
    @Bean
    public Queue queue_image() {
        return new Queue("spring.queue.image", true);
    }

    @Bean
    public Queue queue_pdf() {
        return new Queue("spring.queue.pdf", true);
    }

    // 将消息模板组件RabbitTemplate注入Spring容器
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    // 将消息容器注入Spring容器中
    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queue01(), queue02(), queue03(), queue_image(), queue_pdf());
        container.setConcurrentConsumers(1); // 设置当前消费者数量
        container.setMaxConcurrentConsumers(5); // 设置最大消费者数量
        container.setDefaultRequeueRejected(false); // 是否重回队列
        container.setAcknowledgeMode(AcknowledgeMode.AUTO); // 设置签收模式
        container.setExposeListenerChannel(true); // 设置
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });
        /*container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                System.out.println("消费者：" + new String(message.getBody()));
            }
        });*/

        /*
        * 一：添加消息适配器方式
        * 1.需要自己定义适配器类，默认方法名称为“handleMessage”
        * 2.可以自定设定修改默认方法名称adapter.setDefaultListenerMethod("方法名")
        * 3.可以添加转化器，将字节数组转换为字符穿类型
        * */
        /*MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumerMessage");// 修改自定义Adapter默认方法名称
        adapter.setMessageConverter(new TextMessageConverter());
        container.setMessageListener(adapter);*/

        /*
         * 二：添加消息适配器方式,将队列名称和方法名进行一一绑定
         * */
        /*MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setMessageConverter(new TextMessageConverter());

        // 设置队列和方法名参数。key-队列；value-方法名
        Map<String, String> queueOrTagToMethodName = new HashMap<>();
        queueOrTagToMethodName.put("spring.queue01", "method1");
        queueOrTagToMethodName.put("spring.queue02", "method2");

        adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
        container.setMessageListener(adapter);*/

        // 1.1支持json格式的转换器
        /*MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumerMessage");

        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        adapter.setMessageConverter(jackson2JsonMessageConverter);
        container.setMessageListener(adapter);*/

        // 1.2 支持java对象转换
        /*MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumerMessage");

        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        // 设置Java对象类型
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        javaTypeMapper.setTrustedPackages("com.rabbitmq.domain");
        messageConverter.setJavaTypeMapper(javaTypeMapper);

        adapter.setMessageConverter(messageConverter);
        container.setMessageListener(adapter);*/

        // 1.3 支持java多对象yinshe映射转换
        /*MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumerMessage");

        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();

        // 配置Java对象映射关系集合
        Map<String, Class<?>> idClassMap = new HashMap<>();
        idClassMap.put("order", com.rabbitmq.domain.Order.class);
        idClassMap.put("packaged", com.rabbitmq.domain.Packaged.class);
        javaTypeMapper.setIdClassMapping(idClassMap);

        messageConverter.setJavaTypeMapper(javaTypeMapper);
        adapter.setMessageConverter(messageConverter);
        container.setMessageListener(adapter);*/

        // 1.4 多种数据类型转换
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumerMessage");

        // 全局转换器
        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();

        // 文本转换器
        TextMessageConverter textConverter = new TextMessageConverter();
        converter.addDelegate("text", textConverter);
        converter.addDelegate("html/text", textConverter);
        converter.addDelegate("xml/text", textConverter);
        converter.addDelegate("text/plain", textConverter);

        // Json转换器
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        converter.addDelegate("json", jsonConverter);
        converter.addDelegate("application/json", jsonConverter);

        // 图片转换器
        ImageMessageConverter imageConverter = new ImageMessageConverter();
        converter.addDelegate("image", imageConverter);
        converter.addDelegate("image/jpg", imageConverter);

        // PDF文件转换器
        PdfMessageConverter pdfConverter = new PdfMessageConverter();
        converter.addDelegate("application/pdf", pdfConverter);

        adapter.setMessageConverter(converter);
        container.setMessageListener(adapter);

        return container;

    }

}
