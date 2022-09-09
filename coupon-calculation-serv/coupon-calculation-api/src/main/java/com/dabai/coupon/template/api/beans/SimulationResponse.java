package com.dabai.coupon.template.api.beans;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 订单试算结果，可以看出哪个优惠券的优惠力度最大
 * @author
 * @create 2022-09-06 19:42
 */
@Data
@NoArgsConstructor
public class SimulationResponse {
    /**
     * 最省钱的coupon
     */
    private Long bestCouponId;

    /**
     * 每一个coupon对应的order价格
     */
    private Map<Long, Long> couponToOrderPrice = Maps.newHashMap();

}
