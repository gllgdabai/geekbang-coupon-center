package com.dabai.coupon.calculation.template.impl;

import com.dabai.coupon.calculation.template.AbstractRuleTemplate;
import com.dabai.coupon.calculation.template.RuleTemplate;
import com.dabai.coupon.template.api.beans.ShoppingCart;
import org.springframework.stereotype.Component;

/**
 * 空实现
 * @author
 * @create 2022-09-06 21:39
 */
@Component
public class DummyTemplate extends AbstractRuleTemplate implements RuleTemplate {
    @Override
    public ShoppingCart calculate(ShoppingCart order) {
        // 获取订单总价
        Long orderTotalAmount = getTotalPrice(order.getProducts());
        order.setCost(orderTotalAmount);
        return order;
    }


    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        return orderTotalAmount;
    }
}
