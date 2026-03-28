package com.foodservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodservice.entity.OrdersEntity;

public interface OrdersRepository extends JpaRepository<OrdersEntity, Integer> {
	
}
