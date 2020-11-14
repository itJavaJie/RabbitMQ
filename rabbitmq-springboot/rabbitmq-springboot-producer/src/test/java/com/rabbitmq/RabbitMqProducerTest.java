package com.rabbitmq;

import com.rabbitmq.domain.Order;
import com.rabbitmq.service.RabbitMqSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author FengJie
 * @Date 2020/11/14 20:19
 * @Version 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitMqProducerTest {

    @Autowired
    private RabbitMqSender rabbitMqSender;

    // SpringBoot发送Message消息测试
    @Test
    public void sendMessageTest() throws Exception {

        Map<String, Object> properties = new HashMap<>();
        properties.put("technical", "Java");
        properties.put("middleware", "RabbitMQ");

        String msg = "Hello RabbitMQ Message for SpringBoot!";

        rabbitMqSender.sendMessage(msg, properties);

    }

    // SpringBoot发送Java对象测试
    @Test
    public void sendOrderTest() throws Exception {

        Order order = new Order();
        order.setId("1");
        order.setName("手机");
        order.setDescption("华为手机Mete30Pro+旗舰版+星空黑+16G+128G");

        rabbitMqSender.sendOrder(order);

    }

}
