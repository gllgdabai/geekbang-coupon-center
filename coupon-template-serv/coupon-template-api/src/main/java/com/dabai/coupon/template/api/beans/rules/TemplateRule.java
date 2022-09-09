package com.dabai.coupon.template.api.beans.rules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 包含了两个规则：
 *  1.领券规则：包括每个用户可领取的数量和券模板的过期时间
 *  2.券模板的计算规则
 * @author
 * @create 2022-09-05 21:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRule {
    /**
     * 可以享受的折扣
     */
    private Discount discount;
    /**
     * 每个人最多可用领券的数量
     */
    private Integer limitation;
    /**
     * 过期时间
     */
    private Long deadline;

}
