package com.rabbitmq;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 消费者配置类
 * @Author FengJie
 * @Date 2020/11/14 15:59
 * @Version 1.0.0
 */
@Configuration
@ComponentScan({"com.rabbitmq.*"})
public class ConsumerConfig {
}
