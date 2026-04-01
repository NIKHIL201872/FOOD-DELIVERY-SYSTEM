package com.foodservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foodservice.entity.MenuItemsEntity;

import jakarta.transaction.Transactional;

@Repository
public interface MenuItemsRepository extends JpaRepository<MenuItemsEntity, Integer> {

    // ---------------- RELATION ----------------

    List<MenuItemsEntity> findByRestaurantRestaurantId(Integer restaurantId);

    
    // ---------------- SEARCH ----------------

    List<MenuItemsEntity> findByItemNameContainingIgnoreCase(String name);

    List<MenuItemsEntity> findByItemPriceBetween(Double min, Double max);

    List<MenuItemsEntity> findByRestaurantRestaurantIdAndItemPriceBetween(
            Integer restaurantId, Double min, Double max);

    // ---------------- COMPLEX QUERIES ----------------

    
    
    // Top selling items (based on quantity)
    @Query("SELECT m FROM MenuItemsEntity m " +
           "JOIN OrderItemsEntity oi ON m.itemId = oi.item.itemId " +
           "GROUP BY m.itemId ORDER BY SUM(oi.quantity) DESC")
    List<MenuItemsEntity> findTopSellingItems();

    // Menu items that belong to orders which received this exact rating value
    @Query("SELECT DISTINCT m FROM MenuItemsEntity m " +
           "JOIN OrderItemsEntity oi ON m.itemId = oi.item.itemId " +
           "JOIN RatingEntity r ON oi.order.orderId = r.order.orderId " +
           "WHERE r.rating = :ratingValue")
    List<MenuItemsEntity> findItemsByRating(@Param("ratingValue") int ratingValue);

    // ---------------- DELETE ----------------

    
    
    //----------FIND ITEMS BY RESTAURANT ID AND RATING-------
    @Query("SELECT DISTINCT m, r.rating FROM MenuItemsEntity m " +
           "JOIN OrderItemsEntity oi ON m.itemId = oi.item.itemId " +
           "JOIN RatingEntity r ON oi.order.orderId = r.order.orderId " +
           "WHERE m.restaurant.restaurantId = :restaurantId " +
           "AND r.rating = :minRating")
    	List<Object[]> findItemsByRestaurantAndRating(
    	        @Param("restaurantId") Integer restaurantId,
    	        @Param("minRating") int minRating);
}