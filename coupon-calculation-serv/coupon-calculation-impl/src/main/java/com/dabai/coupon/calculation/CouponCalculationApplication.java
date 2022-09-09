package com.dabai.coupon.calculation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author
 * @create 2022-09-06 20:36
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.dabai"})
@EnableDiscoveryClient
public class CouponCalculationApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponCalculationApplication.class, args);
    }
}
