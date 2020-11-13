package com.rabbitmq.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.util.RabbitmqUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Description RabbitMQ消息详解-消费者
 * @Author FengJie
 * @Date 2020/11/11 16:10
 * @Version 1.0.0
 */
public class ConsumerMessage {

    public static void main(String[] args) {

        Connection connection = null;
        Channel channel = null;
        try {

            // 1.创建连接工厂ConnectionFactory，并设置参数
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("127.0.0.1");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");

            // 2.通过连接工厂创建连接
            connection = connectionFactory.newConnection();

            // 3.通过Connection创建Channel
            channel = connection.createChannel();

            // 4.声明一个队列
            String queueName = "test.quick";
            channel.queueDeclare(queueName, false, false, false, null);

            // 5.创建消费者
            QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

            // 6.设置channel
            channel.basicConsume(queueName, true, queueingConsumer);

            // 7.获取消息队列
            while (true) {
                QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
                System.out.println("消费端接收消息内容：" + new String(delivery.getBody()));

                // 获取消息属性
                Map<String, Object> headerMap = delivery.getProperties().getHeaders();
                System.out.println("消费端接收消息属性：" + headerMap);
                // Envelope envelope = delivery.getEnvelope();

            }

        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } finally {

            // 5.关闭资源，由小到大关闭
            RabbitmqUtil.close(connection, channel);

        }

    }

}
