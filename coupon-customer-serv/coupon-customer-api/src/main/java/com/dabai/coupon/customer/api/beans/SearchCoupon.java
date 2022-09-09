package com.dabai.coupon.customer.api.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 用来封装优惠券查询的请求参数
 * @author
 * @create 2022-09-06 23:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCoupon {
    //用户ID
    @NotNull
    private Long userId;
    //门店ID
    private Long shopId;
    /**
     * 优惠券的状态：1-未使用，2-已用，3-已经注销
     */
    private Integer couponStatus;

}
