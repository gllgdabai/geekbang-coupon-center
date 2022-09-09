package com.dabai.coupon.customer.dao;

import com.dabai.coupon.customer.dao.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author
 * @create 2022-09-07 0:02
 */
public interface CouponDao extends JpaRepository<Coupon, Long> {
    /**
     * 根据用户ID和Template ID，统计用户从当前优惠券模板中领了多少张券
     * @param userId 用户ID
     * @param templateId 券模板ID
     * @return 当前用户从当前优惠券模板中领券数量
     */
    long countByUserIdAndTemplateId(Long userId, Long templateId);

}
