package com.dabai.coupon.customer.dao.convertor;

import com.dabai.coupon.customer.api.enums.CouponStatus;

import javax.persistence.AttributeConverter;

/**
 * @author
 * @create 2022-09-07 0:20
 */
public class CouponStatusConverter implements AttributeConverter<CouponStatus, Integer> {
    // 如果需要把DB里的值转换成enum对象，就采用这种方式就好了
    // 利用泛型模板继承AttributeConverter

    // enum转DB value
    @Override
    public Integer convertToDatabaseColumn(CouponStatus status) {
        return status.getCode();
    }

    // DB value转enum值
    @Override
    public CouponStatus convertToEntityAttribute(Integer code) {
        return CouponStatus.convert(code);
    }
}
