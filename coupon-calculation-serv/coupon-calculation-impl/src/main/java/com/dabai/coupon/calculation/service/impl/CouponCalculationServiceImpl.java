package com.dabai.coupon.calculation.service.impl;

import com.alibaba.fastjson.JSON;
import com.dabai.coupon.calculation.service.CouponCalculationService;
import com.dabai.coupon.calculation.template.CouponTemplateFactory;
import com.dabai.coupon.calculation.template.RuleTemplate;
import com.dabai.coupon.template.api.beans.CouponInfo;
import com.dabai.coupon.template.api.beans.ShoppingCart;
import com.dabai.coupon.template.api.beans.SimulationOrder;
import com.dabai.coupon.template.api.beans.SimulationResponse;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 * @create 2022-09-06 23:18
 */
@Slf4j
@Service
public class CouponCalculationServiceImpl implements CouponCalculationService {

    @Autowired
    private CouponTemplateFactory couponTemplateFactory;

    /**
     * 优惠券结算
     * 这里通过Factory类决定使用哪个底层Rule，底层规则对上层透明
     * @param cart
     * @return
     */
    @Override
    public ShoppingCart calculateOrderPrice(ShoppingCart cart) {
        log.info("calculate order price: {}", JSON.toJSONString(cart));
        RuleTemplate ruleTemplate = couponTemplateFactory.getTemplate(cart);
        return ruleTemplate.calculate(cart);
    }

    /**
     * 对所有优惠券进行试算，看哪个折扣最大
     * @param order
     * @return
     */
    @Override
    public SimulationResponse simulateOrder(SimulationOrder order) {
        SimulationResponse response = new SimulationResponse();
        Long minOrderPrice = Long.MAX_VALUE;

        for (CouponInfo couponInfo : order.getCouponInfos()) {
            ShoppingCart cart = new ShoppingCart();
            cart.setProducts(order.getProducts());
            cart.setCouponInfos(Lists.newArrayList(couponInfo));
            cart = couponTemplateFactory.getTemplate(cart).calculate(cart);

            Long couponId = couponInfo.getId();
            Long orderPrice = cart.getCost();

            // 设置当前优惠券对应的订单价格
            response.getCouponToOrderPrice().put(couponId, orderPrice);

            // 比较订单价格，设置当前最优优惠券的ID
            if (minOrderPrice > orderPrice) {
                response.setBestCouponId(couponInfo.getId());
                minOrderPrice = orderPrice;
            }
        }

        return response;
    }
}
