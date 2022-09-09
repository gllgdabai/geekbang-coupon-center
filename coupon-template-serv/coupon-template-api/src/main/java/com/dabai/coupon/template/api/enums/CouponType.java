package com.dabai.coupon.template.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author
 * @create 2022-09-05 20:48
 */
@Getter
@AllArgsConstructor
public enum CouponType {
    UNKNOWN("unknown", "0"),
    MONEY_OFF("满减券", "1"),
    DISCOUNT("打折", "2"),
    RANDOM_DISCOUNT("随机减", "3"),
    LONELY_NIGHT_MONEY_OFF("晚间双倍优惠券", "4");

    //描述
    private String description;
    //存在数据库里的数据code
    private String code;

    /**
     * 根据优惠券的编码返回对应的枚举对象
     * @param code 编码
     * @return 枚举对象
     */
    public static CouponType convert(String code) {
        return Stream.of(values())
                .filter(bean -> bean.code.equalsIgnoreCase(code))
                .findFirst()
                //用来对付故意输错 code 的恶意请求
                .orElse(UNKNOWN);
    }
}
