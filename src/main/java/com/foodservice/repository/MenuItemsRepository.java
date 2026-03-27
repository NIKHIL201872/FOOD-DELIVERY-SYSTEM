package com.foodservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodservice.entity.MenuItemsEntity;

import jakarta.transaction.Transactional;

public interface MenuItemsRepository extends JpaRepository<MenuItemsEntity, Integer> {

    List<MenuItemsEntity> findByRestaurantRestaurantId(Integer restaurantId);

    @Query("SELECT m FROM MenuItemsEntity m JOIN OrderItemsEntity oi ON m.itemId = oi.item.itemId WHERE oi.order.orderId = :orderId")
    List<MenuItemsEntity> findItemsByOrderId(Integer orderId);

    @Modifying
    @Transactional
    @Query("DELETE FROM MenuItemsEntity m WHERE m.restaurant.restaurantId = :restaurantId")
    void deleteByRestaurantId(Integer restaurantId);
}