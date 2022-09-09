package com.dabai.coupon.customer;

import com.dabai.coupon.customer.loadbalance.CanaryRuleConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author
 * @create 2022-09-07 10:04
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
//由于项目需要加载来自com.dabai.template下的类资源，因此需要额外声明扫包路径
//改为微服务之后，不需要了
//@ComponentScan(basePackages = {"com.dabai"})
//用于扫描Dao @Repository
@EnableJpaRepositories(basePackages = {"com.dabai"})
//用于扫描JPA实体类 @Entity，默认扫本包当下路径
@EntityScan(basePackages = {"com.dabai"})
@EnableDiscoveryClient
//发起到coupon-template-serv的调用，使用CanaryRuleConfiguration中定义的负载均衡Rule
@LoadBalancerClient(value = "coupon-template-serv", configuration = CanaryRuleConfiguration.class)
public class CouponCustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponCustomerApplication.class, args);
    }
}
