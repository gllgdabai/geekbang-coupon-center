package com.dabai.coupon.customer.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * 借助OpenFeign的RequestInterceptor扩展接口
 * 编写一个自定义的拦截器：
 *  在服务请求发送出去之前，往Request的Header里写入一个特殊变量
 * @author
 * @create 2022-09-12 16:07
 */
@Configuration
public class OpenfeignSentinelInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //向服务请求的header里加入一个SentinelSource属性，作为传递给下游服务的“来源标记”
        requestTemplate.header("SentinelSource", "coupon-customer-serv");
    }
}
