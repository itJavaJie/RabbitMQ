package com.rabbitmq.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Description 死信队列-生产者
 * @Author FengJie
 * @Date 2020/11/13 12:30
 * @Version 1.0.0
 */
public class ProducerDlx {

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
            String exchangeName = "test.dlx.exchange";
            String routingKey = "dlx.save";

            // 5.发送消息
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2) //持久化，1-不持久化；2-持久化
                    .contentEncoding("UTF-8")
                    .expiration("5000") // 设置消息的过期时间
                    .build();
            String msg = "Hello RabbitMQ Dlx Message!";
            channel.basicPublish(exchangeName, routingKey, true, properties, msg.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
