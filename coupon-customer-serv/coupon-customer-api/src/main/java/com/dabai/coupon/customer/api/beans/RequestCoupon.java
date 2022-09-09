package com.dabai.coupon.customer.api.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 作为用户领取优惠券的请求参数
 * 通过传入用户ID和优惠券模板ID，用户可以领取一张由指定模板打造的优惠券
 * @author
 * @create 2022-09-06 23:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCoupon {
    /**
     * 用户ID，非空
     */
    @NotNull
    private Long userId;
    /**
     * 券模板ID，非空
     */
    @NotNull
    private Long couponTemplateId;
    /**
     * Loadbalancer - 用作测试流量打标
     */
    private String trafficVersion;
}
