package com.rabbitmq.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.util.RabbitmqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description 模糊连接TopicExchange消费者测试
 * @Author FengJie
 * @Date 2020/11/12 9:42
 * @Version 1.0.0
 */
public class Consumer4TopicExchange {

    public static void main(String[] args) {

        Connection connection = null;
        Channel channel = null;

        try {
            // 1.创建连接工厂ConnectionFactory，并设置参数
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");

            connectionFactory.setAutomaticRecoveryEnabled(true);
            connectionFactory.setNetworkRecoveryInterval(3000);

            // 2.通过连接工厂创建连接
            connection = connectionFactory.newConnection();

            // 3.创建Channel
            channel = connection.createChannel();

            // 4.声明
            String exchangeName = "test.topic.exchange";
            String exchangeType = "topic";
            String queueName = "test.topic.queue";
            //String routingKey = "user.#";
            String routingKey = "user.*";

            // 声明一个交换机
            channel.exchangeDeclare(exchangeName, exchangeType, false, false, false, null);
            // 声明一个队列
            channel.queueDeclare(queueName, false, false, false, null);
            // 建立一个绑定关系
            channel.queueBind(queueName, exchangeName, routingKey);

            // 5.创建消费者
            QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, true, queueingConsumer);

            // 6.接收消息，如果没有消息，会一直阻塞
            while (true) {
                QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
                System.out.println("Topic方式消费者接受消息：" + new String(delivery.getBody()));
            }

        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } finally {

            // 关闭资源
            RabbitmqUtil.close(connection, channel);

        }

    }

}
