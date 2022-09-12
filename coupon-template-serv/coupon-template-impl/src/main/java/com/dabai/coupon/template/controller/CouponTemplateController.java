package com.dabai.coupon.template.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.dabai.coupon.template.api.beans.CouponTemplateInfo;
import com.dabai.coupon.template.api.beans.PagedCouponTemplateInfo;
import com.dabai.coupon.template.api.beans.TemplateSearchParams;
import com.dabai.coupon.template.service.CouponTemplateService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

/**
 * @author
 * @create 2022-09-06 14:55
 */
@Slf4j
@RestController
@RequestMapping("/template")
public class CouponTemplateController {

    @Autowired
    private CouponTemplateService couponTemplateService;

    //创建优惠券
    @PostMapping("/addTemplate")
    public CouponTemplateInfo addTemplate(@Valid @RequestBody CouponTemplateInfo request) {
        log.info("Create coupon template: data={}", request);
        return couponTemplateService.createTemplate(request);
    }

    //克隆优惠券
    @PostMapping("/cloneTemplate")
    public CouponTemplateInfo cloneTemplate(@RequestParam("id") Long templateId) {
        log.info("Clone coupon template: data={}", templateId);
        return couponTemplateService.cloneTemplate(templateId);
    }

    //读取优惠券
    @GetMapping("/getTemplate")
    //标记名称，默认为URl路径：/template/getTemplate
    @SentinelResource(value = "getTemplate")
    public CouponTemplateInfo getTemplate(@RequestParam("id") Long id) {
        log.info("Load template, id={}", id);
        return couponTemplateService.loadTemplateInfo(id);
    }

    //批量获取
    @GetMapping("/getBatch")
    // 你也可以通过defaultFallback属性做一个全局限流、降级的处理逻辑
    // 如果你不想将降级方法写在当前类里，可以通过注解blockHandlerClass和fallbackClass指定"降级类"
    @SentinelResource(
            value = "getTemplateInBatch",
            fallback = "getTemplateInBatch_fallback",
            blockHandler = "getTemplateInBatch_block"
    )
    public Map<Long, CouponTemplateInfo> getTemplateInBatch(@RequestParam("ids") Collection<Long> ids) {
        // 如果接口被熔断，那么下面这行log不会被打印出来
        log.info("getTemplateInBatch: {}", JSON.toJSONString(ids));
        return couponTemplateService.getTemplateInfoMap(ids);
    }

    /**
     * 通用的降级逻辑，应对BlockException以外的各种运行期异常的情况
     */
    public Map<Long, CouponTemplateInfo> getTemplateInBatch_fallback(Collection<Long> ids) {
        log.info("接口被降级");
        return Maps.newHashMap();
    }
    /**
     *  流控降级的方法
     *  只有当前服务抛出了BlockException，才会转而执行该限流方法
     */
    public Map<Long, CouponTemplateInfo> getTemplateInBatch_block(Collection<Long> ids, BlockException exception) {
        log.info("接口被限流");
        return Maps.newHashMap();
    }

    // 搜索模板
    @PostMapping("/search")
    public PagedCouponTemplateInfo search(@Valid @RequestBody TemplateSearchParams request) {
        log.info("search templates, payload={}", request);
        return couponTemplateService.search(request);
    }

    // 优惠券无效化
    @DeleteMapping("/deleteTemplate")
    public void deleteTemplate(@RequestParam("id") Long id){
        log.info("Load template, id={}", id);
        couponTemplateService.deleteTemplate(id);
    }

}
