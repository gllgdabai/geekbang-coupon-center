package com.dabai.coupon.template.service;

import com.dabai.coupon.template.api.beans.CouponTemplateInfo;
import com.dabai.coupon.template.api.beans.PagedCouponTemplateInfo;
import com.dabai.coupon.template.api.beans.TemplateSearchParams;

import java.util.Collection;
import java.util.Map;

/**
 * @author
 * @create 2022-09-06 9:08
 */
public interface CouponTemplateService {

    //创建优惠券模板
    CouponTemplateInfo createTemplate(CouponTemplateInfo request);

    //克隆优惠券模板
    CouponTemplateInfo cloneTemplate(Long templateId);

    //模板查询（分页）
    PagedCouponTemplateInfo search(TemplateSearchParams request);

    //通过模板ID查询优惠券模板
    CouponTemplateInfo loadTemplateInfo(Long id);

    //让优惠券模板失效
    void deleteTemplate(Long id);

    //批量查询, Map中key是模板ID，value是模板详情
    Map<Long, CouponTemplateInfo> getTemplateInfoMap(Collection<Long> ids);

}
