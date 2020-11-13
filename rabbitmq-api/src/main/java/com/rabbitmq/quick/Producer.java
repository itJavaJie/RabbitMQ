package com.rabbitmq.quick;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.util.RabbitmqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description RabbitMQ生产者
 * @Author FengJie
 * @Date 2020/11/11 16:10
 * @Version 1.0.0
 */
public class Producer {

    public static void main(String[] args) {

        ConnectionFactory connectionFactory;
        Connection connection = null;
        Channel channel = null;

        try {
            // 1.创建连接工厂ConnectionFactory，并设置基本参数
            connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("127.0.0.1");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");

            // 2.通过连接工厂创建连接
            connection = connectionFactory.newConnection();

            // 3.通过连接Connection创建Channel
            channel = connection.createChannel();

            // 4.通过Channel发送数据
            for (int i = 0; i < 5; i++) {
                String msg = "Hello RabbitMQ!!!";
                channel.basicPublish("", "test.queue", null, msg.getBytes());
            }

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {

            // 5.最后需要关闭连接，由小到大关闭
            RabbitmqUtil.close(connection, channel);

        }

    }

}
