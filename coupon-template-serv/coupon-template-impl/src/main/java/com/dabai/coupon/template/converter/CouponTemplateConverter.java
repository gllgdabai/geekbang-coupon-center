package com.dabai.coupon.template.converter;

import com.dabai.coupon.template.api.beans.CouponTemplateInfo;
import com.dabai.coupon.template.dao.entity.CouponTemplate;

/**
 * @author
 * @create 2022-09-06 10:05
 */
public class CouponTemplateConverter {

    public static CouponTemplateInfo convertToTemplateInfo(CouponTemplate template) {
        return CouponTemplateInfo.builder()
                .id(template.getId())
                .name(template.getName())
                .desc(template.getDescription())
                .type(template.getCategory().getCode())
                .shopId(template.getShopId())
                .available(template.getAvailable())
                .rule(template.getRule())
                .build();
    }
}
