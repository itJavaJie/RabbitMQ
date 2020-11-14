package com.rabbitmq;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 生产者配置类
 * @Author FengJie
 * @Date 2020/11/14 16:00
 * @Version 1.0.0
 */
@Configuration
@ComponentScan({"com.rabbitmq.*"})
public class ProducerConfig {
}
