package com.dabai.coupon.customer.converter;

import com.dabai.coupon.customer.dao.entity.Coupon;
import com.dabai.coupon.template.api.beans.CouponInfo;

/**
 * @author
 * @create 2022-09-07 10:53
 */
public class CouponConverter {

    public static CouponInfo convertToCouponInfo(Coupon coupon) {
        return CouponInfo.builder()
                .id(coupon.getId())
                .userId(coupon.getUserId())
                .templateId(coupon.getTemplateId())
                .shopId(coupon.getShopId())
                .template(coupon.getTemplateInfo())
                .status(coupon.getStatus().getCode())
                .build();
    }

}
