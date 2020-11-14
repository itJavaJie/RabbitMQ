package com.rabbitmq.service;

import com.rabbitmq.domain.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description TODO
 * @Author FengJie
 * @Date 2020/11/14 16:03
 * @Version 1.0.0
 */
@Component
public class RabbitMqSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 回调函数：消息确认confirm
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("correlationData: " + correlationData);
            System.err.println("ack: " + ack);
            if (ack) {
                System.err.println("消息传输成功，更新数据库等操作。");
            } else {
                System.err.println("消息传输失败，进行补偿或者其他。");
            }
        }
    };

    // 回调函数：返回消息return
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message,
                                    int replyCode,
                                    String replyText,
                                    String exchange,
                                    String routingKey) {
            System.err.println("return error exchange:[" + exchange
                    + "], routingKey:[" + routingKey
                    + "], replyCode:[" + replyCode
                    + "], replyText:[" + replyText + "].");
        }
    };

    /**
     * 生产段发送消息方法调用：构建Message消息
     *
     * @param message
     * @param properties
     * @throws Exception
     */
    public void sendMessage(Object message, Map<String, Object> properties) throws Exception {

        // 设置消息属性
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message, messageHeaders);

        // 设置CorrelationDate
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("1"); // 设置id，一般是id+时间戳，必须保证该条消息的id是全局唯一的

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.convertAndSend("springboot.exchange01", "springboot.hello", msg, correlationData);

    }

    /**
     * 生产段发送消息方法调用：构建Java对象消息
     *
     * @param order
     * @throws Exception
     */
    public void sendOrder(Order order) throws Exception {

        // 设置CorrelationDate
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("2"); // 设置id，一般是id+时间戳，必须保证该条消息的id是全局唯一的

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.convertAndSend("springboot.exchange02", "springboot.abc", order, correlationData);

    }

}
