package com.foodservice.repository;

import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.entity.OrderItemsEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItemsEntity, Integer> {

    @Query("SELECT oi.item FROM OrderItemsEntity oi WHERE oi.order.orderId = :orderId")
    List<MenuItemsEntity> findItemsByOrderId(@Param("orderId") Integer orderId);

    List<OrderItemsEntity> findByOrder_OrderId(Integer orderId);

    List<OrderItemsEntity> findByItem_ItemId(Integer itemId);

    @Query("SELECT COALESCE(MAX(o.orderItemId), 0) FROM OrderItemsEntity o")
    Integer findMaxId();
}