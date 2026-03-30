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

    @Query("SELECT m FROM MenuItemsEntity m " +
           "JOIN OrderItemsEntity oi ON m.itemId = oi.item.itemId " +
           "WHERE oi.order.orderId = :orderId")
    List<MenuItemsEntity> findItemsByOrderId(@Param("orderId") Integer orderId);

    // ---------------- SEARCH ----------------

    List<MenuItemsEntity> findByItemNameContainingIgnoreCase(String name);

    List<MenuItemsEntity> findByItemPriceBetween(Double min, Double max);

    List<MenuItemsEntity> findByRestaurantRestaurantIdAndItemPriceBetween(
            Integer restaurantId, Double min, Double max);

    // ---------------- COMPLEX QUERIES ----------------

    // Items ordered by a specific customer
    @Query("SELECT DISTINCT m FROM MenuItemsEntity m " +
           "JOIN OrderItemsEntity oi ON m.itemId = oi.item.itemId " +
           "JOIN OrdersEntity o ON oi.order.orderId = o.orderId " +
           "WHERE o.customer.customerId = :customerId")
    List<MenuItemsEntity> findItemsByCustomer(@Param("customerId") Integer customerId);

    // Items ordered on a specific date
    @Query("SELECT DISTINCT m FROM MenuItemsEntity m " +
           "JOIN OrderItemsEntity oi ON m.itemId = oi.item.itemId " +
           "JOIN OrdersEntity o ON oi.order.orderId = o.orderId " +
           "WHERE FUNCTION('DATE', o.orderDate) = :date")
    List<MenuItemsEntity> findItemsByOrderDate(@Param("date") String date);

    // Top selling items (based on quantity)
    @Query("SELECT m FROM MenuItemsEntity m " +
           "JOIN OrderItemsEntity oi ON m.itemId = oi.item.itemId " +
           "GROUP BY m.itemId ORDER BY SUM(oi.quantity) DESC")
    List<MenuItemsEntity> findTopSellingItems();

    // Items with rating >= given value
    @Query("SELECT DISTINCT m FROM MenuItemsEntity m " +
    	       "WHERE m.restaurant.restaurantId IN (" +
    	       "SELECT r.restaurant.restaurantId FROM RatingEntity r WHERE r.rating >= :rating)")
    	List<MenuItemsEntity> findItemsByRating(@Param("rating") int rating);

    // ---------------- DELETE ----------------

    @Modifying
    @Transactional
    @Query("DELETE FROM MenuItemsEntity m WHERE m.restaurant.restaurantId = :restaurantId")
    void deleteByRestaurantId(@Param("restaurantId") Integer restaurantId);
    
    
    //----------FIND ITEMS BY RETAURANT ID AND RATING-------
    @Query("SELECT DISTINCT m FROM MenuItemsEntity m " +
    	       "WHERE m.restaurant.restaurantId = :restaurantId " +
    	       "AND m.restaurant.restaurantId IN (" +
    	       "SELECT r.restaurant.restaurantId FROM RatingEntity r " +
    	       "WHERE r.rating >= :minRating)")
    	List<MenuItemsEntity> findItemsByRestaurantAndRating(
    	        @Param("restaurantId") Integer restaurantId,
    	        @Param("minRating") int minRating);
}