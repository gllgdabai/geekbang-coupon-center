package com.dabai.coupon.customer.loadbalance;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * CanaryRule负载均衡规则对应的配置类
 * 需要在启动类上添加一个 @LoadBalancerClient 注解，将该配置类和目标服务关联起来。
 * @author
 * @create 2022-09-09 19:38
 */
// 注意这里不要写上@Configuration注解，原因：不希望把这个负载均衡策略应用到全局
public class CanaryRuleConfiguration {

    @Bean
    public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(
            Environment environment,
            LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        //在Spring上下文中声明了一个CanaryRule规则
        return new CanaryRule(loadBalancerClientFactory
                .getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }


}
