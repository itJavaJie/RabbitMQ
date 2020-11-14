package com.rabbitmq.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.domain.Order;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description TODO
 * @Author FengJie
 * @Date 2020/11/14 22:33
 * @Version 1.0.0
 */
@Component
public class RabbitMqReciver {

    /**
     * 接收普通消息
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue( // 声明需要绑定的队列
                    value = "springboot.queue01",
                    durable = "true"),
            exchange = @Exchange( // 声明需要绑定的交换机
                    value = "springboot.exchange01",
                    durable = "true",
                    type = "topic",
                    ignoreDeclarationExceptions = "true"),
            key = "springboot.#" // 设置路由key
        )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {

        System.err.println("------------------------------------------------");
        System.err.println("消费端接收普通消息：" + message.getPayload());

        // 获取消息Tag
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);

        // 设置手工ACK
        channel.basicAck(deliveryTag, false);

    }

    /**
     * 接收Java对象
     *
     * @param order
     * @param channel
     * @param headers
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    value = "${spring.rabbitmq.listener.order.queue.name}",
                    durable = "${spring.rabbitmq.listener.order.queue.durable}"),
            exchange = @Exchange(
                    value = "${spring.rabbitmq.listener.order.exchange.name}",
                    durable = "${spring.rabbitmq.listener.order.exchange.durable}",
                    type = "${spring.rabbitmq.listener.order.exchange.type}",
                    ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
            key = "${spring.rabbitmq.listener.order.routingkey}"
        )
    )
    @RabbitHandler
    public void onOrderMessage(@Payload com.rabbitmq.domain.Order order,
                               Channel channel, @Headers Map<String, Object> headers) throws Exception {

        System.err.println("------------------------------------------------");
        System.err.println("消费端接收Java对象：" + order);

        // 获取消息Tag
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);

        // 设置手工ACK
        channel.basicAck(deliveryTag, false);

    }

}
