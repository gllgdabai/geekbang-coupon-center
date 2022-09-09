package com.dabai.coupon.calculation.template;

import com.dabai.coupon.template.api.beans.ShoppingCart;

/**
 * @author
 * @create 2022-09-06 20:42
 */
public interface RuleTemplate {
    /**
     * 计算优惠券
     * @param settlement
     * @return
     */
    ShoppingCart calculate(ShoppingCart settlement);
}
