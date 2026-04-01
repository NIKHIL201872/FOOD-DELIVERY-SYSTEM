package com.foodservice.services;

import com.foodservice.DAO.OrdersDAO;
import com.foodservice.dto.CustomerOrderSummary;
import com.foodservice.dto.Orders;
import com.foodservice.dto.PagedResponse;
import com.foodservice.entity.CustomersEntity;
import com.foodservice.entity.DeliveryDriversEntity;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.repository.CustomersRepository;
import com.foodservice.repository.DeliveryDriversRepository;
import com.foodservice.repository.RestaurantsRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersDAO ordersDao;
    private final CustomersRepository customersRepository;
    private final RestaurantsRepository restaurantsRepository;
    private final DeliveryDriversRepository deliveryDriversRepository;

    // ─── CREATE ──────────────────────────────────────────────────────────────

    @Transactional
    public String createOrder(Orders dto) {
        CustomersEntity customer = customersRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found: " + dto.getCustomerId()));

        RestaurantsEntity restaurant = restaurantsRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found: " + dto.getRestaurantId()));

        DeliveryDriversEntity driver = null;
        if (dto.getDeliveryDriverId() != null) {
            driver = deliveryDriversRepository.findById(dto.getDeliveryDriverId())
                    .orElseThrow(() -> new RuntimeException("Driver not found: " + dto.getDeliveryDriverId()));
        }

        OrdersEntity entity = new OrdersEntity();
        int newId = ordersDao.findMaxOrderId() + 1;
        entity.setOrderId(newId);
        entity.setOrderStatus(dto.getOrderStatus() != null ? dto.getOrderStatus() : "PENDING");
        entity.setCustomer(customer);
        entity.setRestaurant(restaurant);
        entity.setDeliveryDriver(driver);
        entity.setOrderDate(dto.getOrderDate());
        ordersDao.save(entity);
        return "Order inserted successfully with ID: " + entity.getOrderId();
    }

    
    // ─── READ ─────────────────────────────────────────────────────────────────

    public Orders getOrderById(Integer orderId) {
        return toDto(ordersDao.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId)));
    }

    public List<Orders> getAllOrders() {
        return ordersDao.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Orders> getOrdersByCustomer(Integer customerId) {
        return ordersDao.findByCustomerId(customerId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Orders> getOrdersByRestaurant(Integer restaurantId) {
        return ordersDao.findByRestaurantId(restaurantId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Orders> getOrdersByDriver(Integer driverId) {
        return ordersDao.findByDriverId(driverId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Orders> getOrdersByStatus(String status) {
        return ordersDao.findByStatus(status).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Orders> getOrdersByCustomerAndStatus(Integer customerId, String status) {
        return ordersDao.findByCustomerIdAndStatus(customerId, status)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Orders> getOrdersByRestaurantAndStatus(Integer restaurantId, String status) {
        return ordersDao.findByRestaurantIdAndStatus(restaurantId, status)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    

    

    public Long countOrdersByRestaurantAndStatus(Integer restaurantId, String status) {
        return ordersDao.countByRestaurantAndStatus(restaurantId, status);
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    @Transactional
    public String updateOrder(Integer orderId, Orders dto) {
        OrdersEntity entity = ordersDao.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        if (dto.getCustomerId() != null) {
            CustomersEntity customer = customersRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + dto.getCustomerId()));
            entity.setCustomer(customer);
        }
        if (dto.getRestaurantId() != null) {
            RestaurantsEntity restaurant = restaurantsRepository.findById(dto.getRestaurantId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found: " + dto.getRestaurantId()));
            entity.setRestaurant(restaurant);
        }
        if (dto.getDeliveryDriverId() != null) {
            DeliveryDriversEntity driver = deliveryDriversRepository.findById(dto.getDeliveryDriverId())
                    .orElseThrow(() -> new RuntimeException("Driver not found: " + dto.getDeliveryDriverId()));
            entity.setDeliveryDriver(driver);
        }
        if (dto.getOrderStatus() != null) {
            entity.setOrderStatus(dto.getOrderStatus());
        }
        if (dto.getOrderDate() != null) {
            entity.setOrderDate(dto.getOrderDate());
        }
        ordersDao.save(entity);
        return "Order updated successfully for ID: " + orderId;
    }

    @Transactional
    public String updateOrderStatus(Integer orderId, String status) {
        OrdersEntity entity = ordersDao.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        entity.setOrderStatus(status);
        ordersDao.save(entity);
        return "Order status updated to '" + status + "' for Order ID: " + orderId;
    }

    @Transactional
    public String assignDriver(Integer orderId, Integer driverId) {
        OrdersEntity entity = ordersDao.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        DeliveryDriversEntity driver = deliveryDriversRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found: " + driverId));
        entity.setDeliveryDriver(driver);
        ordersDao.save(entity);
        return "Driver ID: " + driverId + " assigned successfully to Order ID: " + orderId;
    }

    

    // ─── MAPPER ───────────────────────────────────────────────────────────────

    private Orders toDto(OrdersEntity entity) {
        Orders dto = new Orders();
        dto.setOrderId(entity.getOrderId());
        dto.setOrderStatus(entity.getOrderStatus());
        dto.setOrderDate(entity.getOrderDate());
        dto.setCustomerId(entity.getCustomer() != null ? entity.getCustomer().getCustomerId() : null);
        dto.setRestaurantId(entity.getRestaurant() != null ? entity.getRestaurant().getRestaurantId() : null);
        dto.setDeliveryDriverId(entity.getDeliveryDriver() != null ? entity.getDeliveryDriver().getDriverId() : null);
        return dto;
    }

    // ─── EXTRA ────────────────────────────────────────────────────────────────

        public PagedResponse<Orders> getAllOrdersPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrdersEntity> result = ordersDao.findAllPaged(pageable);
        List<Orders> content = result.getContent()
            .stream().map(this::toDto).collect(Collectors.toList());
        return new PagedResponse<>(
            content,
            result.getNumber(),
            result.getTotalPages(),
            result.getTotalElements(),
            result.getNumberOfElements()
        );
    }
}