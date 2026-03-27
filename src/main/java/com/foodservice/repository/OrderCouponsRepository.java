package com.foodservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.foodservice.entity.*;

public interface OrderCouponsRepository extends JpaRepository<OrderCouponsEntity, OrderCouponsId> {

    // ✅ CORRECT (matches entity mapping)
    List<OrderCouponsEntity> findByOrder_OrderId(Integer orderId);

    // 🔥 Optional (for dynamic coupon-based search)
    List<OrderCouponsEntity> findByCoupon_CouponId(Integer couponId);
    
    void deleteByCoupon_CouponId(Integer couponId);
}