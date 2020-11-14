package com.rabbitmq.converter;

/**
 * @Description TODO
 * @Author FengJie
 * @Date 2020/11/14 11:06
 * @Version 1.0.0
 */
public class ConverterBody {

    private byte[] body;

    public ConverterBody() {
    }

    public ConverterBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

}
