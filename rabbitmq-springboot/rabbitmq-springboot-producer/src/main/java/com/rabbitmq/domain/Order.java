package com.rabbitmq.domain;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author FengJie
 * @Date 2020/11/14 22:57
 * @Version 1.0.0
 */
public class Order implements Serializable {

    private String id;

    private String name;

    private String descption;

    public Order() {
    }

    public Order(String id, String name, String descption) {
        this.id = id;
        this.name = name;
        this.descption = descption;
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

    public String getDescption() {
        return descption;
    }

    public void setDescption(String descption) {
        this.descption = descption;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", descption='" + descption + '\'' +
                '}';
    }
}
