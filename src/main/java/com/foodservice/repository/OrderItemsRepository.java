package com.foodservice.repository;

import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.entity.OrderItemsEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderItemsRepository extends JpaRepository<OrderItemsEntity, Integer> {

    @Query("SELECT oi.item FROM OrderItemsEntity oi WHERE oi.order.orderId = :orderId")
    List<MenuItemsEntity> findItemsByOrderId(@Param("orderId") Integer orderId);

    List<OrderItemsEntity> findByOrder_OrderId(Integer orderId);

    List<OrderItemsEntity> findByItem_ItemId(Integer itemId);

    @Query("SELECT COALESCE(MAX(o.orderItemId), 0) FROM OrderItemsEntity o")
    Integer findMaxId();
 

	 // GET BY ORDER ID + ITEM ID
	 List<OrderItemsEntity> findByOrder_OrderIdAndItem_ItemId(Integer orderId, Integer itemId);
	
	 // GET MOST ORDERED ITEMS
	 @Query("SELECT oi.item.itemId, oi.item.itemName, SUM(oi.quantity) as totalQty, oi.item.itemPrice " +
		       "FROM OrderItemsEntity oi " +
		       "GROUP BY oi.item.itemId, oi.item.itemName, oi.item.itemPrice " +
		       "ORDER BY totalQty DESC")
		List<Object[]> findMostOrderedItems();
	
	 }