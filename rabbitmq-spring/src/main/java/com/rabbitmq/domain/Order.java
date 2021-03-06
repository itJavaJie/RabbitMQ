package com.rabbitmq.domain;

/**
 * @Description 订单实体类
 * @Author FengJie
 * @Date 2020/11/14 9:31
 * @Version 1.0.0
 */
public class Order {

    private String id;

    private String name;

    private String content;

    public Order() {
    }

    public Order(String id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
