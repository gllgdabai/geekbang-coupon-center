package com.dabai.coupon.template.converter;

import com.alibaba.fastjson.JSON;
import com.dabai.coupon.template.api.beans.rules.TemplateRule;

import javax.persistence.AttributeConverter;

/**
 * @author
 * @create 2022-09-05 23:12
 */
public class RuleConverter implements AttributeConverter<TemplateRule, String> {

    @Override
    public String convertToDatabaseColumn(TemplateRule rule) {
        return JSON.toJSONString(rule);
    }

    @Override
    public TemplateRule convertToEntityAttribute(String rule) {
        return JSON.parseObject(rule, TemplateRule.class);
    }
}
