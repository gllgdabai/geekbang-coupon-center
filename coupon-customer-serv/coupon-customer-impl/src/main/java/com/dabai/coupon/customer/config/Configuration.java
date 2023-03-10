package com.dabai.coupon.customer.config;

import feign.Logger;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration注解用于定义配置类
 * 类中定义的Bean方法会被AnnotationConfigApplicationContext和
 * AnnotationConfigWebApplicationContext扫描并初始化
 *
 * @author
 * @create 2022-09-08 23:58
 */
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    //为 WebClient.Build 构造器注入特殊的 Filter，实现负载均衡功能
    @LoadBalanced
    public WebClient.Builder register() {
        return WebClient.builder();
    }

    @Bean
    Logger.Level feignLogger() {
        //指定OpenFeign的日志级别为FULL,在这个级别下所输出的日志文件将会包含最详细的服务调用信息
        return Logger.Level.FULL;
    }
}
