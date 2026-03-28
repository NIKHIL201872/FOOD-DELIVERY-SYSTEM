package com.foodservice.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.foodservice.entity.OrderCouponsEntity;
import com.foodservice.repository.OrderCouponsRepository;

@Repository

public class OrderCouponsDAO {
	@Autowired
     OrderCouponsRepository repo;

    public List<OrderCouponsEntity> findByOrderId(Integer orderId) {
        return repo.findByOrderId(orderId);
    }
}