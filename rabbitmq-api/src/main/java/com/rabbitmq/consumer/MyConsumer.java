package com.rabbitmq.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @Description 自定义消费者
 * @Author FengJie
 * @Date 2020/11/13 12:50
 * @Version 1.0.0
 */
public class MyConsumer extends DefaultConsumer {

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public MyConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

        System.err.println("-----------------Consumer Message---------------");
        System.err.println("consumerTag：" + consumerTag);
        System.err.println("envelope：" + envelope);
        System.err.println("properties：" + properties);
        System.err.println("body：" + new String(body));

    }

}
