package com.dabai.coupon.template.api.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用于封装订单信息
 * @author
 * @create 2022-09-06 19:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    /**
     * 订单的商品列表
     * */
    @NotEmpty
    private List<Product> products;

    private Long couponId;
    /**
     * 封装了优惠券信息，目前计算服务只支持单张优惠券
     * 为了考虑到以后多券的扩展性，所以定义成了List
     */
    private List<CouponInfo> couponInfos;

    /**
     * 订单的最终价格
     */
    private Long cost;
    /**
     * 用户ID
     */
    @NotNull
    private Long userId;



}
