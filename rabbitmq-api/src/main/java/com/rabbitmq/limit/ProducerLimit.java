package com.rabbitmq.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Description 消费端的限流策略-生产者
 * @Author FengJie
 * @Date 2020/11/13 12:30
 * @Version 1.0.0
 */
public class ProducerLimit {

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
            String exchangeName = "test.qos.exchange";
            String routingKey = "qos.save";

            // 5.发送消息
            String msg = "Hello RabbitMQ Send QOS Message Consumer Limit!";
            for (int i = 1; i <= 5; i++) {
                channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
