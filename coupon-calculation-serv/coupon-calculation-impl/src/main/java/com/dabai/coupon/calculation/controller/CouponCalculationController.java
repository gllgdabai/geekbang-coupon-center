package com.dabai.coupon.calculation.controller;

import com.alibaba.fastjson.JSON;
import com.dabai.coupon.calculation.service.CouponCalculationService;
import com.dabai.coupon.template.api.beans.ShoppingCart;
import com.dabai.coupon.template.api.beans.SimulationOrder;
import com.dabai.coupon.template.api.beans.SimulationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 * @create 2022-09-06 23:31
 */
@Slf4j
@RestController
@RequestMapping("calculator")
public class CouponCalculationController {

    @Autowired
    private CouponCalculationService couponCalculationService;

    // 优惠券结算
    @PostMapping("/checkout")
    @ResponseBody
    public ShoppingCart calculateOrderPrice(@RequestBody ShoppingCart settlement) {
        log.info("do calculation: {}", JSON.toJSONString(settlement));
        return couponCalculationService.calculateOrderPrice(settlement);
    }

    // 优惠券列表挨个试算
    // 给客户提示每个可用券的优惠额度，帮助挑选
    @PostMapping("/simulate")
    @ResponseBody
    public SimulationResponse simulate(@RequestBody SimulationOrder simulator) {
        log.info("do simulation: {}", JSON.toJSONString(simulator));
        return couponCalculationService.simulateOrder(simulator);
    }

}
