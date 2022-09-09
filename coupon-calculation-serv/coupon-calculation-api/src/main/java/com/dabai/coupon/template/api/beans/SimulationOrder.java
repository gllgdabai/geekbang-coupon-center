package com.dabai.coupon.template.api.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 优惠券价格试算
 * @author
 * @create 2022-09-06 19:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationOrder {

    @NotEmpty
    private List<Product> products;

    @NotEmpty
    private List<Long> couponIds;
    /**
     * 为空，需要运行试算方法前，进行赋值
     */
    private List<CouponInfo> couponInfos;

    @NotNull
    private Long userId;
}
