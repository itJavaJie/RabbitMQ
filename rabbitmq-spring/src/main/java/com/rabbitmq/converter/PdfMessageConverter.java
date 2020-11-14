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
 * @Description 文件类型转换器
 * @Author FengJie
 * @Date 2020/11/14 11:07
 * @Version 1.0.0
 */
public class PdfMessageConverter implements MessageConverter {

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        throw new MessageConversionException("Convert Error!");
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.out.println("---------------------------Pdf MessageConverter----------------------------");

        // 获取图片内容
        byte[] body = message.getBody();

        // 定义图片名称和存储路径
        String fileName = UUID.randomUUID().toString();
        String path = "E:\\document\\file\\" + fileName + ".pdf";

        File file = new File(path);

        try {
            Files.copy(new ByteArrayInputStream(body), file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

}
