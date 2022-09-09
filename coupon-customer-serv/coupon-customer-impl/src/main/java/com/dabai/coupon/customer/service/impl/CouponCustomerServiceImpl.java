package com.dabai.coupon.customer.service.impl;

import com.dabai.coupon.customer.api.beans.RequestCoupon;
import com.dabai.coupon.customer.api.beans.SearchCoupon;
import com.dabai.coupon.customer.api.enums.CouponStatus;
import com.dabai.coupon.customer.converter.CouponConverter;
import com.dabai.coupon.customer.dao.CouponDao;
import com.dabai.coupon.customer.dao.entity.Coupon;
import com.dabai.coupon.customer.service.CouponCustomerService;
import com.dabai.coupon.template.api.beans.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dabai.coupon.customer.constant.Constant.TRAFFIC_VERSION;

/**
 * @author
 * @create 2022-09-07 10:22
 */
@Slf4j
@Service
public class CouponCustomerServiceImpl implements CouponCustomerService {

    @Autowired
    private CouponDao couponDao;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public Coupon requestCoupon(RequestCoupon request) {
//        CouponTemplateInfo templateInfo = couponTemplateService.loadTemplateInfo(request.getCouponTemplateId());
        CouponTemplateInfo templateInfo = webClientBuilder.build()
                //指明了Http Method是GET
                .get()
                //指定访问的请求地址
                .uri("http://coupon-template-serv/template/getTemplate?id=" + request.getCouponTemplateId())
                //将流量标记传入WebClient请求的Header中
                .header(TRAFFIC_VERSION, request.getTrafficVersion())
                //retrieve + bodyToMono：指定了 Response 的返回格式
                .retrieve()
                .bodyToMono(CouponTemplateInfo.class)
                //发起一个阻塞调用，在远程服务没有响应之前，当前线程处于阻塞状态
                .block();

        if (templateInfo == null) {
            log.error("invalid template id = {}", request.getCouponTemplateId());
            throw new IllegalArgumentException("Invalid template id");
        }

        //检查：模板是否过期
        long now = Calendar.getInstance().getTimeInMillis();
        Long deadline = templateInfo.getRule().getDeadline();
        if (deadline != null && now >= deadline || BooleanUtils.isFalse(templateInfo.getAvailable())) {
            log.error("template is not available id = {}", request.getCouponTemplateId());
            throw new IllegalArgumentException("template is unavailable");
        }

        //检查：用户领券数量是否超过上限
        long count = couponDao.countByUserIdAndTemplateId(request.getUserId(), request.getCouponTemplateId());
        if (count >= templateInfo.getRule().getLimitation()) {
            log.error("exceeds maximum number");
            throw new IllegalArgumentException("exceeds maximum number");
        }

        Coupon coupon = Coupon.builder()
                .templateId(request.getCouponTemplateId())
                .userId(request.getUserId())
                .shopId(templateInfo.getShopId())
                .status(CouponStatus.AVAILABLE)
                .build();
        couponDao.save(coupon);

        return coupon;
    }

    @Override
    @Transactional
    public ShoppingCart placeOrder(ShoppingCart order) {
        //购物车为空，抛出异常
        if (CollectionUtils.isEmpty(order.getProducts())) {
            log.error("invalid check out request, order={}", order);
            throw new IllegalArgumentException("cart is empty");
        }

        Coupon coupon = null;
        if (order.getCouponId() != null) {
            //如果有优惠券，检查是否可用，并且属于当前客户
            //构建一个查询模板对象
            Coupon example = Coupon.builder()
                    .userId(order.getUserId())
                    .id(order.getCouponId())
                    .status(CouponStatus.AVAILABLE)
                    .build();
            List<Coupon> coupons = couponDao.findAll(Example.of(example));
            coupon = coupons.stream()
                    .findFirst()
                    //如果找不到券，则抛出异常
                    .orElseThrow(() -> new RuntimeException("Coupon not found"));
            //优惠券有了，再把它的券模板信息查出
            //券模板里的Discount规则会在稍后用于订单价格计算
            CouponInfo couponInfo = CouponConverter.convertToCouponInfo(coupon);
//            couponInfo.setTemplate(couponTemplateService.loadTemplateInfo(coupon.getTemplateId()));
            CouponTemplateInfo templateInfo = webClientBuilder.build()
                    .get()
                    .uri("http://coupon-template-serv/template/getTemplate?id=" + coupon.getTemplateId())
                    .retrieve()
                    .bodyToMono(CouponTemplateInfo.class)
                    .block();
            couponInfo.setTemplate(templateInfo);
            order.setCouponInfos(Lists.newArrayList(couponInfo));
        }

        //order清算，调用calculation服务使用优惠后的订单价格
//        ShoppingCart checkoutInfo = couponCalculationService.calculateOrderPrice(order);
        ShoppingCart checkoutInfo = webClientBuilder.build().post()
                .uri("http://coupon-calculation-serv/calculator/checkout")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(ShoppingCart.class)
                .block();


        if (coupon != null) {
            //如果优惠券没有被结算掉，而用户传递了优惠券，报错提示该订单满足不了优惠条件
            if (CollectionUtils.isEmpty(checkoutInfo.getCouponInfos())) {
                log.error("cannot apply coupon to order, couponId={}", coupon.getId());
                throw new IllegalArgumentException("coupon is not applicable to this order");
            }

            log.info("update coupon status to used, couponId={}", coupon.getId());
            coupon.setStatus(CouponStatus.USED);
            couponDao.save(coupon);
        }

        return checkoutInfo;
    }

    @Override
    public SimulationResponse simulateOrderPrice(SimulationOrder order) {
        List<CouponInfo> couponInfos = Lists.newArrayList();
        //循环，把优惠券信息加载出来
        //高并发场景下不能这么一个一个循环，更好的做法是批量查询
        //而且券模板一旦创建不会修改内容，所以在创建端做数据异构放到缓存里，使用端从缓存捞template信息
        for (Long couponId : order.getCouponIds()) {
            Coupon example = Coupon.builder()
                    .id(couponId)
                    .userId(order.getUserId())
                    .status(CouponStatus.AVAILABLE)
                    .build();
            Optional<Coupon> couponOptional = couponDao.findAll(Example.of(example)).stream().findFirst();
            //加载优惠券模板信息
            if (couponOptional.isPresent()) {
                Coupon coupon = couponOptional.get();
                CouponInfo couponInfo = CouponConverter.convertToCouponInfo(coupon);
//                couponInfo.setTemplate(couponTemplateService.loadTemplateInfo(coupon.getTemplateId()));
                CouponTemplateInfo templateInfo = webClientBuilder.build()
                        .get()
                        .uri("http://coupon-template-serv/template/getTemplate?id=" + coupon.getTemplateId())
                        .retrieve()
                        .bodyToMono(CouponTemplateInfo.class)
                        .block();
                couponInfo.setTemplate(templateInfo);
                couponInfos.add(couponInfo);
            }
        }
        //对couponInfos属性进行赋值后，才可进行试算
        order.setCouponInfos(couponInfos);

        //调用接口试算服务
//        return couponCalculationService.simulateOrder(order);
        return webClientBuilder.build().post()
                .uri("http://coupon-calculation-serv/calculator/simulate")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(SimulationResponse.class)
                .block();
    }

    /**
     * 逻辑删除优惠券，即把status改为已注销
     * @param userId
     * @param couponId
     */
    @Override
    public void deleteCoupon(Long userId, Long couponId) {
        Coupon example = Coupon.builder()
                .userId(userId)
                .id(couponId)
                .status(CouponStatus.AVAILABLE)
                .build();
        Coupon coupon = couponDao.findAll(Example.of(example))
                .stream()
                .findFirst()
                // 如果找不到券，就抛出异常
                .orElseThrow(() -> new RuntimeException("Could not find available coupon"));

        coupon.setStatus(CouponStatus.INACTIVE);
        couponDao.save(coupon);
    }

    @Override
    public List<CouponInfo> findCoupon(SearchCoupon request) {
        //在真实的生产环境，这个接口需要做分页查询，并且查询的条件要封装成一个类
        Coupon example = Coupon.builder()
                .userId(request.getUserId())
                .shopId(request.getShopId())
                .status(CouponStatus.convert(request.getCouponStatus()))
                .build();

        //TODO: 尝试去实现分页查询
        List<Coupon> coupons = couponDao.findAll(Example.of(example));
        if (coupons.isEmpty()) {
            return Lists.newArrayList();
        }

        //获取coupons中的templateId集合
        List<Long> templateIds = coupons.stream()
                .map(Coupon::getTemplateId)
                .collect(Collectors.toList());
//        Map<Long, CouponTemplateInfo> templateInfoMap = couponTemplateService.getTemplateInfoMap(templateIds);

        Map<Long, CouponTemplateInfo> templateInfoMap = webClientBuilder.build().get()
                .uri("http://coupon-template-serv/template/getBatch?ids=" + templateIds)
                .retrieve()
                //由于方法的返回值不是一个标准的Json对象，需要构造一个ParameterizedTypeReference实例，说明需要将Response转化成什么类型
                .bodyToMono(new ParameterizedTypeReference<Map<Long, CouponTemplateInfo>>() {})
                .block();

        //把templateId对应的模板详情templateInfo，赋值给coupon对象的templateInfo属性
        coupons.stream()
                .forEach(e -> e.setTemplateInfo(templateInfoMap.get(e.getTemplateId())));

        return coupons.stream()
                //把Coupon对象转换成CouponInfo对象
                .map(CouponConverter::convertToCouponInfo)
                .collect(Collectors.toList());
    }
}
