package com.rabbitmq.converter;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

/**
 * @Description 图片类型转换器
 * @Author FengJie
 * @Date 2020/11/14 11:07
 * @Version 1.0.0
 */
public class ImageMessageConverter implements MessageConverter {

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        throw new MessageConversionException("Converter Error!");
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {

        System.out.println("---------------------------Image MessageConverter----------------------------");

        // 获取图片后缀名
        Object _extName = message.getMessageProperties().getHeaders().get("extName");
        String extName = _extName == null ? "jpg" : _extName.toString();

        // 获取图片内容
        byte[] body = message.getBody();

        // 定义图片名称和存储路径
        String fileName = UUID.randomUUID().toString();
        String path = "E:\\document\\image\\" + fileName + "." + extName;

        File file = new File(path);

        try {
            Files.copy(new ByteArrayInputStream(body), file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

}
