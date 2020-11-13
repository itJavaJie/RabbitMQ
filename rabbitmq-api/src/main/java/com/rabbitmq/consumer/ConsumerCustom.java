package com.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Description 自定义消费者-消费者
 * @Author FengJie
 * @Date 2020/11/13 12:30
 * @Version 1.0.0
 */
public class ConsumerCustom {

    public static void main(String[] args) {

        try {

            // 1.创建连接工厂
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");

            // 2.创建连接
            Connection connection = connectionFactory.newConnection();

            // 3.通过连接创建Channel
            Channel channel = connection.createChannel();

            // 4.声明交换机和队列，绑定交换机和队列，最后设定路由key
            String exchangeName = "test.consumer.exchange";
            String routingKey = "consumer.#";
            String queueName = "test.consumer.queue";
            // 声明交换机
            channel.exchangeDeclare(exchangeName, "topic", false);
            // 声明队列
            channel.queueDeclare(queueName, false, false, false, null);
            // 绑定交换机和队列
            channel.queueBind(queueName, exchangeName, routingKey);

            // 5.自定义消费者
            channel.basicConsume(queueName, true, new MyConsumer(channel));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
