package com.dabai.coupon.customer.feign.fallback;

import com.dabai.coupon.customer.feign.TemplateService;
import com.dabai.coupon.template.api.beans.CouponTemplateInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * 如果想要在降级方法中获取到异常的具体原因
 * 就需要借助 fallback 工厂的方式来指定降级逻辑
 * @author
 * @create 2022-09-10 23:59
 */
@Slf4j
@Component
public class TemplateServiceFallbackFactory implements FallbackFactory<TemplateService> {

    @Override
    public TemplateService create(Throwable cause) {
        //使用这种方法可以捕获到具体的异常cause
        return new TemplateService() {
            @Override
            public CouponTemplateInfo getTemplate(Long id) {
                log.info("fallback factory method test");
                return null;
            }

            @Override
            public Map<Long, CouponTemplateInfo> getTemplateInBatch(Collection<Long> ids) {
                log.info("fallback factory method test");
                return null;
            }
        };
    }
}
