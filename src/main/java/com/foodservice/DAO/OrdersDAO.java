package com.foodservice.DAO;

import com.foodservice.entity.OrdersEntity;
import com.foodservice.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrdersDAO {

    private final OrdersRepository ordersRepository;

    public OrdersEntity save(OrdersEntity order) {
        return ordersRepository.save(order);
    }
    public Integer findMaxOrderId() {
        return ordersRepository.findMaxOrderId();
    }

    public Optional<OrdersEntity> findById(Integer orderId) {
        return ordersRepository.findById(orderId);
    }

    public List<OrdersEntity> findAll() {
        return ordersRepository.findAll();
    }

    public void deleteById(Integer orderId) {
        ordersRepository.deleteById(orderId);
    }

    public boolean existsById(Integer orderId) {
        return ordersRepository.existsById(orderId);
    }

    public List<OrdersEntity> findByCustomerId(Integer customerId) {
        return ordersRepository.findByCustomer_CustomerId(customerId);
    }

    public List<OrdersEntity> findByRestaurantId(Integer restaurantId) {
        return ordersRepository.findByRestaurant_RestaurantId(restaurantId);
    }

    public List<OrdersEntity> findByDriverId(Integer driverId) {
        return ordersRepository.findByDeliveryDriver_DriverId(driverId);
    }

    public List<OrdersEntity> findByStatus(String status) {
        return ordersRepository.findByOrderStatus(status);
    }

    public List<OrdersEntity> findByCustomerIdAndStatus(Integer customerId, String status) {
        return ordersRepository.findByCustomer_CustomerIdAndOrderStatus(customerId, status);
    }

    public List<OrdersEntity> findByRestaurantIdAndStatus(Integer restaurantId, String status) {
        return ordersRepository.findByRestaurant_RestaurantIdAndOrderStatus(restaurantId, status);
    }

    public List<OrdersEntity> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return ordersRepository.findByOrderDateBetween(startDate, endDate);
    }

    public List<OrdersEntity> findByRestaurantAndDateRange(Integer restaurantId,
                                                           LocalDateTime startDate,
                                                           LocalDateTime endDate) {
        return ordersRepository.findByRestaurantAndDateRange(restaurantId, startDate, endDate);
    }

    public Long countByRestaurantAndStatus(Integer restaurantId, String status) {
        return ordersRepository.countByRestaurantAndStatus(restaurantId, status);
    }

    public boolean existsByOrderIdAndCustomerId(Integer orderId, Integer customerId) {
        return ordersRepository.existsByOrderIdAndCustomer_CustomerId(orderId, customerId);
    }
 // OrdersDAO.java - add these

    public List<OrdersEntity> findPendingUnassigned() {
        return ordersRepository.findPendingUnassigned();
    }

    public List<OrdersEntity> findActiveByDriver(Integer driverId) {
        return ordersRepository.findByDeliveryDriver_DriverIdAndOrderStatus(driverId, "OUT_FOR_DELIVERY");
    }

    public Double sumRevenueByRestaurantAndDateRange(Integer restaurantId,
                                                      LocalDateTime startDate,
                                                      LocalDateTime endDate) {
        return ordersRepository.sumRevenueByRestaurantAndDateRange(restaurantId, startDate, endDate);
    }

    public Long countByCustomerId(Integer customerId) {
        return ordersRepository.countByCustomer_CustomerId(customerId);
    }

    public Object[] findTopRestaurantByCustomer(Integer customerId) {
        return ordersRepository.findTopRestaurantByCustomer(customerId);
    }
    public Page<OrdersEntity> findAllPaged(Pageable pageable) {
        return ordersRepository.findAll(pageable);
    }
}