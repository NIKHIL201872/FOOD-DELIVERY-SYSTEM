package com.foodservice.repository;

import com.foodservice.entity.DeliveryDriversEntity;
import com.foodservice.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersEntity, Integer> {
	@Query("SELECT COALESCE(MAX(o.orderId), 0) FROM OrdersEntity o")
	Integer findMaxOrderId();

	List<OrdersEntity> findByDeliveryDriver(DeliveryDriversEntity driver);
    // Find all orders by customer
    List<OrdersEntity> findByCustomer_CustomerId(Integer customerId);

    // Find all orders by restaurant
    List<OrdersEntity> findByRestaurant_RestaurantId(Integer restaurantId);

    // Find all orders by delivery driver
    List<OrdersEntity> findByDeliveryDriver_DriverId(Integer driverId);

    // Find orders by status
    List<OrdersEntity> findByOrderStatus(String orderStatus);

    // Find orders by customer and status
    List<OrdersEntity> findByCustomer_CustomerIdAndOrderStatus(Integer customerId, String orderStatus);

    // Find orders by restaurant and status
    List<OrdersEntity> findByRestaurant_RestaurantIdAndOrderStatus(Integer restaurantId, String orderStatus);

    // Find orders placed within a date range
    List<OrdersEntity> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find orders by restaurant within a date range
    @Query("SELECT o FROM OrdersEntity o WHERE o.restaurant.restaurantId = :restaurantId " +
           "AND o.orderDate BETWEEN :startDate AND :endDate")
    List<OrdersEntity> findByRestaurantAndDateRange(@Param("restaurantId") Integer restaurantId,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    // Count orders by status for a specific restaurant
    @Query("SELECT COUNT(o) FROM OrdersEntity o WHERE o.restaurant.restaurantId = :restaurantId " +
           "AND o.orderStatus = :status")
    Long countByRestaurantAndStatus(@Param("restaurantId") Integer restaurantId,
                                   @Param("status") String status);

    // Check if an order belongs to a given customer (for ownership/security checks)
    boolean existsByOrderIdAndCustomer_CustomerId(Integer orderId, Integer customerId);
    
 // OrdersRepository.java - add these

 // Pending orders with no driver assigned
 @Query("SELECT o FROM OrdersEntity o WHERE o.orderStatus = 'PENDING' AND o.deliveryDriver IS NULL")
 List<OrdersEntity> findPendingUnassigned();

 // Active deliveries for a driver
 List<OrdersEntity> findByDeliveryDriver_DriverIdAndOrderStatus(Integer driverId, String status);

 // Revenue sum for a restaurant in a date range (requires joining orderItems)
 @Query("SELECT SUM(oi.quantity * oi.item.itemPrice) FROM OrderItemsEntity oi " +
        "WHERE oi.order.restaurant.restaurantId = :restaurantId " +
        "AND oi.order.orderDate BETWEEN :startDate AND :endDate " +
        "AND oi.order.orderStatus = 'DELIVERED'")
 Double sumRevenueByRestaurantAndDateRange(@Param("restaurantId") Integer restaurantId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

 // Customer summary - count of orders
 Long countByCustomer_CustomerId(Integer customerId);

 // Most ordered restaurant for a customer
 @Query(value = "SELECT o.restaurant_id, COUNT(o.order_id) as cnt FROM orders o " +
        "WHERE o.customer_id = :customerId " +
        "GROUP BY o.restaurant_id ORDER BY cnt DESC LIMIT 1", nativeQuery = true)
 Object[] findTopRestaurantByCustomer(@Param("customerId") Integer customerId);
}