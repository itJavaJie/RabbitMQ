package com.rabbitmq.returnlistener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @Description Return返回消息-消费者
 * @Author FengJie
 * @Date 2020/11/13 11:25
 * @Version 1.0.0
 */
public class ConsumerReturn {

    public static void main(String[] args) {

        try {
            // 1.创建连接工厂
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setVirtualHost("/");
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);

            // 2.创建连接
            Connection connection = connectionFactory.newConnection();

            // 3.通过连接创建Channel
            Channel channel = connection.createChannel();

            // 4.生命交换机和队列，绑定交换机规则，最后设置路由key
            String exchangeName = "test.return.exchange";
            String queueName = "test.return.queue";
            String routingKey = "return.#";
            // 声明交换机
            channel.exchangeDeclare(exchangeName, "topic", false);
            // 声明队列
            channel.queueDeclare(queueName, false, false, false, null);
            // 绑定交换机和队列
            channel.queueBind(queueName, exchangeName, routingKey);

            // 5.创建消费者
            QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, true, queueingConsumer);

            // 6.接收消息
            while (true) {
                QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
                System.out.println("消费者接收消息：" + new String(delivery.getBody()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
