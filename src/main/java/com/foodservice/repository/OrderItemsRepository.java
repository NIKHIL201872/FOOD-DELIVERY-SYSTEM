package com.foodservice.repository;

import com.foodservice.entity.OrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItemsEntity, Integer> {

	@Query("SELECT oi FROM OrderItemsEntity oi WHERE oi.order.orderId = :orderId")
	List<OrderItemsEntity> getOrderItems(Integer orderId);
	List<OrderItemsEntity> findByOrderOrderId(Integer orderId);

    List<OrderItemsEntity> findByItemItemId(Integer itemId);

}