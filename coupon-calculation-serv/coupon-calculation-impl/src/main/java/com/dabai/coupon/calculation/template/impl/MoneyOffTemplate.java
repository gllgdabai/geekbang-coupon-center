package com.dabai.coupon.calculation.template.impl;

import com.dabai.coupon.calculation.template.AbstractRuleTemplate;
import com.dabai.coupon.calculation.template.RuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 满减优惠券计算规则
 * @author
 * @create 2022-09-06 21:46
 */
@Slf4j
@Component
public class MoneyOffTemplate extends AbstractRuleTemplate implements RuleTemplate {

    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        // benefitAmount是扣减的价格
        // 如果当前门店的商品总价<quota，那么最多只能扣减shopAmount的钱数
        Long benefitAmount = shopTotalAmount < quota ? shopTotalAmount : quota;
        return orderTotalAmount - benefitAmount;
    }
}
