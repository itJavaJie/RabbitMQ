package com.rabbitmq.confirmlistener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description Confirm确认消息-消费者
 * @Author FengJie
 * @Date 2020/11/13 10:50
 * @Version 1.0.0
 */
public class ConsumerConfirm {

    public static void main(String[] args) {

        Connection connection = null;
        Channel channel = null;

        try {
            // 1.创建连接工场ConnectionFactory，并设置参数
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");

            // 2.创建连接
            connection = connectionFactory.newConnection();

            // 3.通过连接创建Channel
            channel = connection.createChannel();

            String exchangeName = "test.confirm.exchange";
            String routingKey = "confirm.#";
            String queueName = "test.confirm.queue";

            // 4.声明交换机和队列，绑定交换机和队列，最后设置路由key
            // 声明exchange
            channel.exchangeDeclare(exchangeName, "topic", false);
            // 声明队列queue
            channel.queueDeclare(queueName, false, false, false, null);
            // 绑定exchange和queue
            channel.queueBind(queueName, exchangeName, routingKey);

            // 5.创建消费者
            QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, true, queueingConsumer);

            // 6.接收消息
            while (true) {
                QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
                System.out.println("消费端接收消息：" + new String(delivery.getBody()));
            }

        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } finally {

            // RabbitmqUtil.close(connection, channel);

        }

    }

}
