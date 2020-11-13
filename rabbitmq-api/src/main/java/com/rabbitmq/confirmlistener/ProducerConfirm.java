package com.rabbitmq.confirmlistener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description Confirm确认消息-生产者
 * @Author FengJie
 * @Date 2020/11/13 10:50
 * @Version 1.0.0
 */
public class ProducerConfirm {

    public static void main(String[] args) {

        Connection connection = null;
        Channel channel = null;

        try {
            // 1.创建ConnectionFactory，并设置参数
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/");

            // 2.创建连接Connection
            connection = connectionFactory.newConnection();

            // 3.通过Connection创建Channel
            channel = connection.createChannel();

            // 4.指定消息投递模式
            channel.confirmSelect();

            String exchangeName = "test.confirm.exchange";
            String routingKey = "confirm.save";

            // 5.发送一条消息
            String msg = "Hello RabbitMQ Send Message Confirm!";
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

            // 6.添加一个确认监听
            channel.addConfirmListener(new ConfirmListener() {

                // 返回成功进入此方法
                @Override
                public void handleAck(long deliveryTag, boolean mulpitle) throws IOException {
                    System.out.println("------------------Ack------------------");
                }

                // 返回失败进入此方法
                @Override
                public void handleNack(long deliveryTag, boolean mulpitle) throws IOException {
                    System.out.println("----------------No Ack-----------------");
                }
            });

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {

            // RabbitmqUtil.close(connection, channel);

        }

    }

}
