package com.foodservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodservice.entity.OrderItemsEntity;

public interface OrderItemsRepository extends JpaRepository<OrderItemsEntity, Integer> {

}
