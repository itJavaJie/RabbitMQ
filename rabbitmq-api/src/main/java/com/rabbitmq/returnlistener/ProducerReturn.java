package com.rabbitmq.returnlistener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;

import java.io.IOException;

/**
 * @Description Return返回消息-生产者
 * @Author FengJie
 * @Date 2020/11/13 11:26
 * @Version 1.0.0
 */
public class ProducerReturn {

    public static void main(String[] args) {

        try {
            // 1.创建连接工厂，并设置参数
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");

            // 2.创建连接
            Connection connection = connectionFactory.newConnection();

            // 3.通过连接创建Channel
            Channel channel = connection.createChannel();

            // 4.声明交换机和路由
            String exchangeName = "test.return.exchange";
            String routingKey = "return.save";
            String routingErrorKey = "error.save";

            // 5.设置消息return监听
            channel.addReturnListener(new ReturnListener() {
                @Override
                public void handleReturn(int replyCode,
                                         String replyText,
                                         String exchange,
                                         String routingKey,
                                         AMQP.BasicProperties properties,
                                         byte[] body)
                        throws IOException {
                    System.out.println("-----------------Handler Return-----------------");
                    System.out.println("replyCode：" + replyCode);
                    System.out.println("replyText：" + replyText);
                    System.out.println("exchange：" + exchange);
                    System.out.println("routingKey：" + routingKey);
                    System.out.println("properties：" + properties);
                    System.out.println("body：" + new String(body));
                }
            });

            // 6.发送消息
            String msg = "Hello RabbitMQ Send Message Return!";
            // channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
            channel.basicPublish(exchangeName, routingErrorKey, true, null, msg.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
