package com.rabbitmq.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Description 消费端ACK和重回队列机制-消费者
 * @Author FengJie
 * @Date 2020/11/13 12:30
 * @Version 1.0.0
 */
public class ConsumerAck {

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
            String exchangeName = "test.ack.exchange";
            String routingKey = "ack.#";
            String queueName = "test.ack.queue";
            // 声明交换机
            channel.exchangeDeclare(exchangeName, "topic", false);
            // 声明队列
            channel.queueDeclare(queueName, false, false, false, null);
            // 绑定交换机和队列
            channel.queueBind(queueName, exchangeName, routingKey);

            /*
            * 5.自定义消费者接收消息：
            * 1)如果使用手工签收，那么basicConsume()方法中的参数“autoAck”一定要设置为false，关闭自动签收才能生效
            * 2)
            * */
            channel.basicConsume(queueName, false, new MyConsumer(channel));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
