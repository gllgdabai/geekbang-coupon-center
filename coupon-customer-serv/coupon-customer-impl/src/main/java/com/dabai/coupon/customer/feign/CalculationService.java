package com.dabai.coupon.customer.feign;

import com.dabai.coupon.template.api.beans.ShoppingCart;
import com.dabai.coupon.template.api.beans.SimulationOrder;
import com.dabai.coupon.template.api.beans.SimulationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * OpenFeign组件通过 接口代理 的方式发起远程调用
 * 实现对coupon-calculation-serv的远程调用代理
 * @author
 * @create 2022-09-10 0:14
 */
@FeignClient(value = "coupon-calculation-serv", path = "/calculator")
public interface CalculationService {

    @PostMapping("/checkout")
    ShoppingCart checkout(ShoppingCart settlement);

    @PostMapping("/simulate")
    SimulationResponse simulate(SimulationOrder simulator);

}
