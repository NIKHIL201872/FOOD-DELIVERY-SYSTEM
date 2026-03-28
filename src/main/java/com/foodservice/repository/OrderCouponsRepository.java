package com.foodservice.repository;

import com.foodservice.entity.OrderCouponsEntity;
import com.foodservice.entity.OrderCouponsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderCouponsRepository extends JpaRepository<OrderCouponsEntity, OrderCouponsId> {

    List<OrderCouponsEntity> findByOrderId(Integer orderId);
}