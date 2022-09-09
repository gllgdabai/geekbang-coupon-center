package com.dabai.coupon.template.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装优惠券信息
 * @author
 * @create 2022-09-05 22:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponInfo {

    private Long id;

    private Long templateId;
    /** 领券用户 ID */
    private Long userId;
    /** 适用门店 ID */
    private Long shopId;

    private Integer status;
    /** 优惠券的模板信息 */
    private CouponTemplateInfo template;

}
