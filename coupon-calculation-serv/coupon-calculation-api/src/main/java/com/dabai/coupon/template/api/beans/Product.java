package com.dabai.coupon.template.api.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用来封装订单的商品信息
 * @author
 * @create 2022-09-06 19:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    /** 商品的价格 */
    private long price;

    /** 商品在购物车里的数量 */
    private Integer count;

    /** 商品销售的门店 */
    private Long shopId;
}
