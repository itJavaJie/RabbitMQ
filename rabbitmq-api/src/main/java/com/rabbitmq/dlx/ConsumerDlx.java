package com.rabbitmq.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 死信队列-消费者
 * @Author FengJie
 * @Date 2020/11/13 12:30
 * @Version 1.0.0
 */
public class ConsumerDlx {

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
            // 创建死信队列，但是下方三个声明字符串就是一个单传的队列和交换机
            String exchangeName = "test.dlx.exchange";
            String routingKey = "dlx.#";
            String queueName = "test.dlx.queue";
            // 声明交换机
            channel.exchangeDeclare(exchangeName, "topic", false);

            // 声明队列
            /*
            * 在channel.queueDeclare()方法中设置死信队列参数：
            * 1)必须将arguments参数设置在声明队列上，不能设置在声明交换机上
            * */
            // 设置死信队列参数
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange", "dlx.exchange");

            channel.queueDeclare(queueName, false, false, false, arguments);
            // 绑定交换机和队列
            channel.queueBind(queueName, exchangeName, routingKey);

            /*
            * 2)死信队列设置：声明死信队列的交换机和队列，将交换机和队列进行绑定
            * */
            channel.exchangeDeclare("dlx.exchange", "topic", false, false, null);
            channel.queueDeclare("dlx.queue", false, false, false, null);
            channel.queueBind("dlx.queue", "dlx.exchange", "#");

            // 5.自定义消费者
            channel.basicConsume(queueName, true, new MyConsumer(channel));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
