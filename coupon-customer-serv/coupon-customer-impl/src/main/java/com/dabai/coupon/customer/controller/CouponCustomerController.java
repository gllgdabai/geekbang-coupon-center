package com.dabai.coupon.customer.controller;

import com.dabai.coupon.customer.api.beans.RequestCoupon;
import com.dabai.coupon.customer.api.beans.SearchCoupon;
import com.dabai.coupon.customer.dao.entity.Coupon;
import com.dabai.coupon.customer.service.CouponCustomerService;
import com.dabai.coupon.template.api.beans.CouponInfo;
import com.dabai.coupon.template.api.beans.ShoppingCart;
import com.dabai.coupon.template.api.beans.SimulationOrder;
import com.dabai.coupon.template.api.beans.SimulationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author
 * @create 2022-09-07 15:32
 */
@Slf4j
@RestController
@RequestMapping("coupon-customer")
//NacosConfig中的属性变动动态同步到当前类的变量
@RefreshScope
public class CouponCustomerController {

    @Value("${disableCouponRequest:false}")
    private Boolean disableCoupon;

    @Autowired
    private CouponCustomerService couponCustomerService;

    @PostMapping("requestCoupon")
    public Coupon requestCoupon(@Valid @RequestBody RequestCoupon request) {
        if (disableCoupon) {
            log.info("暂停领取优惠券");
            return null;
        }
        return couponCustomerService.requestCoupon(request);
    }

    // 用户删除优惠券 - 逻辑删除
    @DeleteMapping("deleteCoupon")
    public void deleteCoupon(@RequestParam("userId") Long userId,
                             @RequestParam("couponId") Long couponId) {
        couponCustomerService.deleteCoupon(userId, couponId);
    }

    // 用户模拟计算每个优惠券的优惠价格
    @PostMapping("simulateOrder")
    public SimulationResponse simulate(@Valid @RequestBody SimulationOrder order) {
        return couponCustomerService.simulateOrderPrice(order);
    }

    // ResponseEntity - 指定返回状态码 - 可以作为一个课后思考题
    // 下单核销优惠券
    @PostMapping("placeOrder")
    public ShoppingCart checkout(@Valid @RequestBody ShoppingCart info) {
        return couponCustomerService.placeOrder(info);
    }


    // 实现的时候最好封装一个search object类
    @PostMapping("findCoupon")
    public List<CouponInfo> findCoupon(@Valid @RequestBody SearchCoupon request) {
        return couponCustomerService.findCoupon(request);
    }


}
