package com.dabai.coupon.template.api.beans;

import com.dabai.coupon.template.api.beans.rules.TemplateRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 创建优惠券模板
 * @author
 * @create 2022-09-05 21:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponTemplateInfo {

    private Long id;

    /** 优惠券名称 */
    @NotNull
    private String name;

    /** 优惠券描述 */
    @NotNull
    private String desc;

    /** 优惠券类型(引用CouponType里的code) */
    @NotNull
    private String type;

    /** 适用门店 - 若无则为全店通用券 */
    private Long shopId;

    /** 优惠券规则 */
    @NotNull
    private TemplateRule rule;

    /** 当前模板是否为可用状态 */
    private Boolean available;
}
