package com.rabbitmq.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.util.RabbitmqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description 任意连接TopicExchange消费者测试
 * @Author FengJie
 * @Date 2020/11/12 9:42
 * @Version 1.0.0
 */
public class Producer4FanoutExchange {

    public static void main(String[] args) {

        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Channel channel = null;

        try {
            // 1.创建连接工厂ConnectionFactory，并设置参数
            connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");

            // 2.创建连接Connection
            connection = connectionFactory.newConnection();

            // 3.通过连接创建Channel
            channel = connection.createChannel();

            // 4.声明
            String exchangeName = "test.fanout.exchange";

            // 5.发送消息
            for (int i = 1; i <= 10; i++) {
                String msg = "Hello World RabbitMQ 4 Fanout Exchange Message.";
                channel.basicPublish(exchangeName, "", null, msg.getBytes());
            }

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {

            // 6.关闭资源
            RabbitmqUtil.close(connection, channel);

        }

    }

}
