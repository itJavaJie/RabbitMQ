package com.rabbitmq.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 消费端ACK和重回队列机制-生产者
 * @Author FengJie
 * @Date 2020/11/13 12:30
 * @Version 1.0.0
 */
public class ProducerAck {

    public static void main(String[] args) {

        try {
            // 1.创建连接工厂，设置参数
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");

            // 2.创建连接
            Connection connection = connectionFactory.newConnection();

            // 3.通过连接创建Channel
            Channel channel = connection.createChannel();

            // 4.声明交换机和路由key
            String exchangeName = "test.ack.exchange";
            String routingKey = "ack.save";

            // 5.发送消息，设置消息属性
            for (int i = 1; i <= 5; i++) {

                Map<String, Object> hesdersMap = new HashMap<>();
                hesdersMap.put("num", i);
                hesdersMap.put("technical", "Java" + i);
                hesdersMap.put("middleWare", "RabbitMQ" + i);

                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                        .deliveryMode(2)
                        .contentEncoding("UTF-8")
                        .headers(hesdersMap)
                        .build();

                String msg = "Hello RabbitMQ Send QOS Message Consumer Limit " + i;
                channel.basicPublish(exchangeName, routingKey, true, properties, msg.getBytes());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
