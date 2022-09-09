package com.dabai.coupon.calculation.service;

import com.dabai.coupon.template.api.beans.ShoppingCart;
import com.dabai.coupon.template.api.beans.SimulationOrder;
import com.dabai.coupon.template.api.beans.SimulationResponse;

/**
 * @author
 * @create 2022-09-06 23:16
 */
public interface CouponCalculationService {

    ShoppingCart calculateOrderPrice(ShoppingCart cart);

    SimulationResponse simulateOrder(SimulationOrder cart);

}
