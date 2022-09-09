package com.dabai.coupon.template.service.impl;

import com.dabai.coupon.template.dao.CouponTemplateDao;
import com.dabai.coupon.template.api.beans.CouponTemplateInfo;
import com.dabai.coupon.template.api.beans.PagedCouponTemplateInfo;
import com.dabai.coupon.template.api.beans.TemplateSearchParams;
import com.dabai.coupon.template.converter.CouponTemplateConverter;
import com.dabai.coupon.template.dao.entity.CouponTemplate;
import com.dabai.coupon.template.api.enums.CouponType;
import com.dabai.coupon.template.service.CouponTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author
 * @create 2022-09-06 9:30
 */
@Slf4j
@Service
public class CouponTemplateServiceImpl implements CouponTemplateService {

    @Autowired
    private CouponTemplateDao couponTemplateDao;

    @Override
    public CouponTemplateInfo createTemplate(CouponTemplateInfo request) {
        //规定：单个门店最多可用创建100张优惠券模板
        if (request.getShopId() != null) {
            Integer count = couponTemplateDao.countByShopIdAndAvailable(request.getShopId(), true);
            if (count >= 100) {
                log.error("the totals of coupon template exceeds maximum number");
                throw new UnsupportedOperationException("exceeded the maximum of coupon templates that you can create");
            }
        }

        //创建优惠券
        CouponTemplate template = CouponTemplate.builder()
                .name(request.getName())
                .description(request.getDesc())
                .category(CouponType.convert(request.getType()))
                .available(true)
                .shopId(request.getShopId())
                .rule(request.getRule())
                .build();
        log.info("create template {}", template);
        template = couponTemplateDao.save(template);

        return CouponTemplateConverter.convertToTemplateInfo(template);
    }

    @Override
    public CouponTemplateInfo cloneTemplate(Long templateId) {
        log.info("cloning template id {}", templateId);
        CouponTemplate source = couponTemplateDao.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("invalid template ID"));

        CouponTemplate target = new CouponTemplate();
        BeanUtils.copyProperties(source, target);

        target.setAvailable(true);
        //为什么要让Id为null呢？因为Id与source重复了，并且save插入，自动生成id
        target.setId(null);

        couponTemplateDao.save(target);

        return CouponTemplateConverter.convertToTemplateInfo(target);
    }

    @Override
    public PagedCouponTemplateInfo search(TemplateSearchParams request) {
        log.info("searchParams {}", request);
        //通过Example对象查询：构造一个模板对象，使用 findAll 方法来查询；
        CouponTemplate example = CouponTemplate.builder()
                .shopId(request.getShopId())
                .category(CouponType.convert(request.getType()))
                .available(request.getAvailable())
                .name(request.getName())
                .build();

        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getPageSize());
        Page<CouponTemplate> templates = couponTemplateDao.findAll(Example.of(example), pageRequest);
        List<CouponTemplateInfo> couponTemplateInfos = templates.stream()
                //把Stream (CouponTemplate>)转化为 另一个Stream (CouponTemplateInfo)
                .map(CouponTemplateConverter::convertToTemplateInfo)
                //输出为List
                .collect(Collectors.toList());
        //构造一个用来实现分页查询的PagedCouponTemplateInfo对象
        PagedCouponTemplateInfo response = PagedCouponTemplateInfo.builder()
                .templates(couponTemplateInfos)
                .page(request.getPage())
                .total(templates.getTotalElements())
                .build();

        return response;
    }

    @Override
    public CouponTemplateInfo loadTemplateInfo(Long id) {
        Optional<CouponTemplate> template = couponTemplateDao.findById(id);

//        return template.isPresent() ? CouponTemplateConverter.convertToTemplateInfo(template.get()) : null;
        return template.map(CouponTemplateConverter::convertToTemplateInfo).orElse(null);
    }

    @Override
    public void deleteTemplate(Long id) {
        int rows = couponTemplateDao.makeCouponUnavailable(id);
        if (rows == 0) {
            throw new IllegalArgumentException("Template Not Found: " + id);
        }
    }

    @Override
    public Map<Long, CouponTemplateInfo> getTemplateInfoMap(Collection<Long> ids) {

        List<CouponTemplate> templates = couponTemplateDao.findAllById(ids);

        return templates.stream()
                .map(CouponTemplateConverter::convertToTemplateInfo)
                //输出为Map
                .collect(Collectors.toMap(
                        CouponTemplateInfo::getId,
                        Function.identity()
                ));
    }
}
