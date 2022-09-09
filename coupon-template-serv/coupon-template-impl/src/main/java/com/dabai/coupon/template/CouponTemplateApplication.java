package com.dabai.coupon.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author
 * @create 2022-09-06 15:21
 */
@SpringBootApplication
//给MySQL列属性自动赋值,例如一些创建时间，修改时间
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.dabai"})
@EnableDiscoveryClient
public class CouponTemplateApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponTemplateApplication.class, args);
    }
}