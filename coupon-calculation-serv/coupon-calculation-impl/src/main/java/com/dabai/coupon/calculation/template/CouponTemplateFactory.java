package com.dabai.coupon.calculation.template;

import com.dabai.coupon.calculation.template.impl.*;
import com.dabai.coupon.template.api.beans.CouponTemplateInfo;
import com.dabai.coupon.template.api.beans.ShoppingCart;
import com.dabai.coupon.template.api.enums.CouponType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工厂方法
 * 根据订单中的优惠券类型，返回对应的Template，用于计算优惠价
 * @author
 * @create 2022-09-06 21:59
 */
@Slf4j
@Component
public class CouponTemplateFactory {

    @Autowired
    private MoneyOffTemplate moneyOffTemplate;

    @Autowired
    private DiscountTemplate discountTemplate;

    @Autowired
    private RandomReductionTemplate randomReductionTemplate;

    @Autowired
    private LonelyNightTemplate lonelyNightTemplate;

    @Autowired
    private DummyTemplate dummyTemplate;

    public RuleTemplate getTemplate(ShoppingCart order) {
        //不使用优惠券
        if (CollectionUtils.isEmpty(order.getCouponInfos())) {
            //dummy模板直接返回原价，不进行优惠计算
            return dummyTemplate;
        }

        //获取优惠券的类别
        //目前每个订单只支持单张优惠券
        CouponTemplateInfo template = order.getCouponInfos().get(0).getTemplate();
        CouponType category = CouponType.convert(template.getType());

        switch (category) {
            //订单满减券
            case MONEY_OFF: return moneyOffTemplate;
            //随机立减券
            case RANDOM_DISCOUNT: return randomReductionTemplate;
            //晚间双倍优惠券
            case LONELY_NIGHT_MONEY_OFF: return lonelyNightTemplate;
            //打折券
            case DISCOUNT: return discountTemplate;
            //未知类型的券模板
            default: return dummyTemplate;
        }
    }

}
