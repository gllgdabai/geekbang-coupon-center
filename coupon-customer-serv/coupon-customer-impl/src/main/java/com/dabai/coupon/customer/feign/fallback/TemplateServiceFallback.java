package com.dabai.coupon.customer.feign.fallback;

import com.dabai.coupon.customer.feign.TemplateService;
import com.dabai.coupon.template.api.beans.CouponTemplateInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * 降级类：
 * OpenFeign+Hystrix 的 Client 端降级方案
 * @author
 * @create 2022-09-10 23:50
 */
@Slf4j
@Component
public class TemplateServiceFallback implements TemplateService {
    @Override
    public CouponTemplateInfo getTemplate(Long id) {
        log.info("fallback getTemplate");
        return null;
    }

    @Override
    public Map<Long, CouponTemplateInfo> getTemplateInBatch(Collection<Long> ids) {
        log.info("fallback getTemplateInBatch");
        return null;
    }
}
