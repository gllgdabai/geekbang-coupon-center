package com.dabai.coupon.customer.feign;

import com.dabai.coupon.customer.feign.fallback.TemplateServiceFallbackFactory;
import com.dabai.coupon.template.api.beans.CouponTemplateInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Map;

/**
 * OpenFeign组件通过 接口代理 的方式发起远程调用
 * 实现对coupon-template-serv的远程调用代理
 * @author
 * @create 2022-09-09 23:47
 */
@FeignClient(value = "coupon-template-serv", path = "/template",
        //通过抽象工厂来定义降级逻辑
        fallbackFactory = TemplateServiceFallbackFactory.class)
public interface TemplateService {

    @GetMapping("/getTemplate")
    CouponTemplateInfo getTemplate(@RequestParam("id") Long id);

    @GetMapping("/getBatch")
    Map<Long, CouponTemplateInfo> getTemplateInBatch(@RequestParam("ids") Collection<Long> ids);

}
