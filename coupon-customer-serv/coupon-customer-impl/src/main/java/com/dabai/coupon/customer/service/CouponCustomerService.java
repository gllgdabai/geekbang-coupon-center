package com.dabai.coupon.customer.service;

import com.dabai.coupon.customer.api.beans.RequestCoupon;
import com.dabai.coupon.customer.api.beans.SearchCoupon;
import com.dabai.coupon.customer.dao.entity.Coupon;
import com.dabai.coupon.template.api.beans.CouponInfo;
import com.dabai.coupon.template.api.beans.ShoppingCart;
import com.dabai.coupon.template.api.beans.SimulationOrder;
import com.dabai.coupon.template.api.beans.SimulationResponse;

import java.util.List;

/**
 * 用户对接服务
 * @author
 * @create 2022-09-07 10:09
 */
public interface CouponCustomerService {
    /**
     * 领券接口
     * @param request
     * @return
     */
    Coupon requestCoupon(RequestCoupon request);

    /**
     * 核销优惠券
     * @return
     */
    ShoppingCart placeOrder(ShoppingCart order);

    /**
     * 优惠券金额试算
     * @param order
     * @return
     */
    SimulationResponse simulateOrderPrice(SimulationOrder order);

    /**
     * 用户删除优惠券
     * @param userId
     * @param couponId
     */
    void deleteCoupon(Long userId, Long couponId);

    /**
     * 查询用户优惠券
     * @param request
     * @return
     */
    List<CouponInfo> findCoupon(SearchCoupon request);

}
