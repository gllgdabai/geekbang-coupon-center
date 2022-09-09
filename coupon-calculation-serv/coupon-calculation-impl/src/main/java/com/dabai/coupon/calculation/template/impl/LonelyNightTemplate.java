package com.dabai.coupon.calculation.template.impl;

import com.dabai.coupon.calculation.template.AbstractRuleTemplate;
import com.dabai.coupon.calculation.template.RuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * 午夜10点到次日凌晨2点之间下单，优惠金额翻倍
 * @author
 * @create 2022-09-06 21:41
 */
@Slf4j
@Component
public class LonelyNightTemplate extends AbstractRuleTemplate implements RuleTemplate {


    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (hourOfDay >= 23 || hourOfDay < 2) {
            quota *= 2;
        }
        // benefitAmount是扣减的价格
        // 如果当前门店的商品总价 < quota，那么最多只能扣减shopAmount的钱数
        Long benefitAmount = shopTotalAmount < quota ? shopTotalAmount : quota;
        return orderTotalAmount - benefitAmount;
    }
}
