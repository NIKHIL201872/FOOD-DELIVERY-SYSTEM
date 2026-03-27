package com.foodservice.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.foodservice.entity.OrderItemsEntity;
import com.foodservice.repository.OrderItemsRepository;

@Repository

public class OrderItemsDAO {
	 @Autowired
     OrderItemsRepository repo;

    public List<OrderItemsEntity> findByOrderId(Integer orderId) {
        return repo.findByOrderId(orderId);
    }
}
