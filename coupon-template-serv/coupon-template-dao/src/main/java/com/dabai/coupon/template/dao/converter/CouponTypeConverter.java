package com.dabai.coupon.template.converter;

import com.dabai.coupon.template.api.enums.CouponType;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * @author
 * @create 2022-09-05 23:04
 */
@Convert
public class CouponTypeConverter implements AttributeConverter<CouponType, String> {

    @Override
    public String convertToDatabaseColumn(CouponType couponCategory) {
        return couponCategory.getCode();
    }

    @Override
    public CouponType convertToEntityAttribute(String code) {
        return CouponType.convert(code);
    }
}
