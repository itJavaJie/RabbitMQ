package com.rabbitmq.limit;

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

    private Channel channel;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

        System.err.println("-----------------Consumer Message---------------");
        System.err.println("consumerTag：" + consumerTag);
        System.err.println("envelope：" + envelope);
        System.err.println("properties：" + properties);
        System.err.println("body：" + new String(body));

        /*
        * 使用channel设置ACK，参数解释：
        * 1)deliveryTag：
        * 2)multiple：是否批量签收（true:是；false:否）,根据消费端【channel.basicQos(0, 1, false);】方法中设置的prefetchCount消息个数来设置
        * */
        channel.basicAck(envelope.getDeliveryTag(), false);

    }

}
