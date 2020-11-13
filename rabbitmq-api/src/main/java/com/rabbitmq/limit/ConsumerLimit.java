package com.rabbitmq.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Description 消费端的限流策略-消费者
 * @Author FengJie
 * @Date 2020/11/13 12:30
 * @Version 1.0.0
 */
public class ConsumerLimit {

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
            String exchangeName = "test.qos.exchange";
            String routingKey = "qos.#";
            String queueName = "test.qos.queue";
            // 声明交换机
            channel.exchangeDeclare(exchangeName, "topic", false);
            // 声明队列
            channel.queueDeclare(queueName, false, false, false, null);
            // 绑定交换机和队列
            channel.queueBind(queueName, exchangeName, routingKey);

            /*
            * 5.自定义消费者接收消息：
            * 1)如果使用限流方式，那么basicConsume()方法中的参数“autoAck”一定要设置为false，关闭自动签收才能生效
            * 2)使用channel设置参数:
            *   prefetchSize(当前消息的大小限制)
            *   prefetchCount(最多每次只给消费端N个消息)
            *   global(将前两个设置应用到channel(true)级别好事consumer级别(false))
            * */
            channel.basicQos(0, 1, false);
            channel.basicConsume(queueName, false, new MyConsumer(channel));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
