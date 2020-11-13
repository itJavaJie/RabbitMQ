package com.rabbitmq.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.util.RabbitmqUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Description RabbitMQ消息详解-生产者
 * @Author FengJie
 * @Date 2020/11/11 16:10
 * @Version 1.0.0
 */
public class ProducerMessage {

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

            // 4.添加消息的附加属性
            Map<String, Object> headMap = new HashMap<>();
            headMap.put("technical", "Java");
            headMap.put("middleWare", "Rabbit");

            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2) //持久化，1-不持久化；2-持久化
                    .contentEncoding("UTF-8")
                    .expiration("10000") // 设置消息的过期时间
                    .headers(headMap)
                    .build();

            // 4.通过Channel发送数据
            for (int i = 1; i <= 5; i++) {
                String msg = "Hello RabbitMQ!";
                channel.basicPublish("", "test.quick", properties, msg.getBytes());
            }

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {

            // 5.最后需要关闭连接，由小到大关闭
            RabbitmqUtil.close(connection, channel);

        }

    }

}
