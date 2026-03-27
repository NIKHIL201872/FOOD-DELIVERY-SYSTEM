package com.foodservice.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.foodservice.DAO.DeliveryDriversDAO;
import com.foodservice.DAO.OrderCouponsDAO;
import com.foodservice.DAO.OrderItemsDAO;
import com.foodservice.DAO.OrdersDAO;
import com.foodservice.dto.Orders;
import com.foodservice.entity.CustomersEntity;
import com.foodservice.entity.DeliveryDriversEntity;
import com.foodservice.entity.OrderCouponsEntity;
import com.foodservice.entity.OrderItemsEntity;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.exception.ResourceNotFoundException;
import com.foodservice.repository.CustomersRepository;
import com.foodservice.repository.RestaurantsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersDAO orderDAO;
    private final CustomersRepository customerRepo;
    private final RestaurantsRepository restaurantRepo;
    private final DeliveryDriversDAO driverDAO;
    private final OrderItemsDAO orderItemsDAO;
    private final OrderCouponsDAO orderCouponsDAO;

    // CREATE ORDER
    public Orders createOrder(Orders dto) {

        CustomersEntity customer = customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        RestaurantsEntity restaurant = restaurantRepo.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        OrdersEntity order = new OrdersEntity();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setOrderStatus("CREATED");

        OrdersEntity saved = orderDAO.save(order);

        Orders dt = new Orders();
        dt.setOrderId(saved.getOrderId());
        dt.setOrderStatus(saved.getOrderStatus());
        dt.setCustomerId(saved.getCustomer().getCustomerId());
        dt.setRestaurantId(saved.getRestaurant().getRestaurantId());

        return dt;
    }

    // ASSIGN DRIVER
    public void assignDriver(Integer orderId, Integer driverId) {

        OrdersEntity order = orderDAO.findById(orderId);
        DeliveryDriversEntity driver = driverDAO.findById(driverId);

        order.setDeliveryDriver(driver);
        orderDAO.save(order);
    }

    // UPDATE STATUS
    public void updateStatus(Integer orderId, String status) {

        OrdersEntity order = orderDAO.findById(orderId);
        order.setOrderStatus(status);

        orderDAO.save(order);
    }

    // GET ORDER
    public Orders getOrder(Integer orderId) {

        OrdersEntity order = orderDAO.findById(orderId);

        Orders d = new Orders();
        d.setOrderId(order.getOrderId());
        d.setOrderStatus(order.getOrderStatus());
        d.setCustomerId(order.getCustomer().getCustomerId());
        d.setRestaurantId(order.getRestaurant().getRestaurantId());

        return d;
    }

    // FULL DETAILS
    public Map<String, Object> getFullOrderDetails(Integer orderId) {

        OrdersEntity order = orderDAO.findById(orderId);

        List<OrderItemsEntity> items = orderItemsDAO.findByOrderId(orderId);
        List<OrderCouponsEntity> coupons = orderCouponsDAO.findByOrderId(orderId);

        Map<String, Object> res = new HashMap<>();
        res.put("order", order);
        res.put("items", items);
        res.put("coupons", coupons);

        return res;
    }
}