package com.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description 工具类
 * @Author FengJie
 * @Date 2020/11/12 9:53
 * @Version 1.0.0
 */
public class RabbitmqUtil {

    /**
     * 关闭RaMbbitQ连接资源
     *
     * @param connection
     * @param channel
     */
    public static void close(Connection connection, Channel channel) {

        try {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

}
