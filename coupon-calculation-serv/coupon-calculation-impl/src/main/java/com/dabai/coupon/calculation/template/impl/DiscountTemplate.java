package com.dabai.coupon.calculation.template.impl;

import com.dabai.coupon.calculation.template.AbstractRuleTemplate;
import com.dabai.coupon.calculation.template.RuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 打折优惠券
 * @author
 * @create 2022-09-06 21:34
 */
@Slf4j
@Component
public class DiscountTemplate extends AbstractRuleTemplate implements RuleTemplate {

    @Override
    protected Long calculateNewPrice(Long totalAmount, Long shopAmount, Long quota) {
        //计算使用优惠券之后的价格
        Long newPrice = convertToDecimal(shopAmount * (quota.doubleValue()/100));
        log.debug("original price={}, new price={}", totalAmount, totalAmount - shopAmount + newPrice);
        return newPrice;
    }
}
