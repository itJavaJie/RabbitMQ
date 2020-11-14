package com.rabbitmq.adapter;

import com.rabbitmq.domain.Order;
import com.rabbitmq.domain.Packaged;

import java.io.File;
import java.util.Map;

/**
 * @Description 自定义适配器
 * @Author FengJie
 * @Date 2020/11/14 9:25
 * @Version 1.0.0
 */
public class MessageDelegate {

    /**
     * 消息适配器默认方法
     * @param messageBody
     */
    public void handleMessage(byte[] messageBody) {
        System.err.println("默认方法，消息内容：" + new String(messageBody));
    }

    /**
     * 消息适配器设置方式一 - 修改默认方法：
     * adapter.setDefaultListenerMethod("方法名");
     *
     * @param messageBody
     */
    public void consumerMessage(byte[] messageBody) {
        System.err.println("字节数组方法，消息内容：" + new String(messageBody));
    }

    /**
     * 消息适配器设置方式一 - 修改默认方法，添加数据转换器：
     * adapter.setDefaultListenerMethod("方法名");
     * adapter.setMessageConverter(new TextMessageConverter());
     *
     * @param messageBody
     */
    public void consumerMessage(String messageBody) {
        System.err.println("字符串方法，消息内容：" + messageBody);
    }

    /**
     * 消息适配器设置方式二 - 适配器设置队列和方法名绑定方式：
     * adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
     *
     * @param messageBody
     */
    public void method1(String messageBody) {
        System.err.println("Method1收到消息内容：" + messageBody);
    }

    /**
     * 消息适配器设置方式二 - 适配器设置队列和方法名绑定方式：
     * adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
     *
     * @param messageBody
     */
    public void method2(String messageBody) {
        System.err.println("Method2收到消息内容：" + messageBody);
    }

    /**
     * 消息转换器：Json格式转换
     *
     * @param messageBody
     */
    public void consumerMessage(Map messageBody) {
        System.err.println("Map方法，消息内容：" + messageBody);
    }

    /**
     * 消息转换器：文件格式转换
     *
     * @param messageBody
     */
    public void consumerMessage(File messageBody) {
        System.err.println("文件对象方法，消息内容：" + messageBody);
    }

    /**
     * 消息转换器：对象格式转换
     *
     * @param messageBody
     */
    public void consumerMessage(Order messageBody) {
        System.err.println("Order对象方法，消息内容：" + messageBody);
    }

    /**
     * 消息转换器：对象格式转换
     *
     * @param messageBody
     */
    public void consumerMessage(Packaged messageBody) {
        System.err.println("Packaged对象方法，消息内容：" + messageBody);
    }

}
