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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private OrdersDAO ordersDao;

    @Mock
    private CustomersRepository customersRepository;

    @Mock
    private RestaurantsRepository restaurantsRepository;

    @Mock
    private DeliveryDriversRepository deliveryDriversRepository;

    @InjectMocks
    private OrderService service;

    private CustomersEntity customer;
    private RestaurantsEntity restaurant;
    private DeliveryDriversEntity driver;
    private OrdersEntity orderEntity;
    private Orders dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new CustomersEntity();
        customer.setCustomerId(101);

        restaurant = new RestaurantsEntity();
        restaurant.setRestaurantId(201);

        driver = new DeliveryDriversEntity();
        driver.setDriverId(301);

        orderEntity = new OrdersEntity();
        orderEntity.setOrderId(1);
        orderEntity.setCustomer(customer);
        orderEntity.setRestaurant(restaurant);
        orderEntity.setDeliveryDriver(driver);
        orderEntity.setOrderStatus("PENDING");
        orderEntity.setOrderDate(LocalDateTime.of(2026, 3, 30, 10, 0));

        dto = new Orders(1, 101, 201, 301, "PENDING", LocalDateTime.of(2026, 3, 30, 10, 0));
    }

    @Test
    void createOrder_returnsSuccessMessage() {
        when(customersRepository.findById(101)).thenReturn(Optional.of(customer));
        when(restaurantsRepository.findById(201)).thenReturn(Optional.of(restaurant));
        when(deliveryDriversRepository.findById(301)).thenReturn(Optional.of(driver));
        when(ordersDao.findMaxOrderId()).thenReturn(10);
        when(ordersDao.save(any(OrdersEntity.class))).thenReturn(orderEntity);

        String result = service.createOrder(dto);

        assertEquals("Order inserted successfully with ID: 11", result);
        verify(ordersDao).save(any(OrdersEntity.class));
    }

    

    @Test
    void getOrderById_returnsDto() {
        when(ordersDao.findById(1)).thenReturn(Optional.of(orderEntity));

        Orders result = service.getOrderById(1);

        assertEquals(1, result.getOrderId());
        assertEquals(101, result.getCustomerId());
        assertEquals(201, result.getRestaurantId());
    }

    @Test
    void getAllOrders_returnsList() {
        when(ordersDao.findAll()).thenReturn(List.of(orderEntity));

        List<Orders> result = service.getAllOrders();

        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getOrderStatus());
    }

    @Test
    void updateOrderStatus_updatesAndReturnsMessage() {
        when(ordersDao.findById(1)).thenReturn(Optional.of(orderEntity));
        when(ordersDao.save(orderEntity)).thenReturn(orderEntity);

        String result = service.updateOrderStatus(1, "OUT_FOR_DELIVERY");

        assertEquals("Order status updated to 'OUT_FOR_DELIVERY' for Order ID: 1", result);
        assertEquals("OUT_FOR_DELIVERY", orderEntity.getOrderStatus());
        verify(ordersDao).save(orderEntity);
    }

    @Test
    void assignDriver_updatesDriverAndReturnsMessage() {
        OrdersEntity noDriverOrder = new OrdersEntity();
        noDriverOrder.setOrderId(1);
        noDriverOrder.setCustomer(customer);
        noDriverOrder.setRestaurant(restaurant);
        noDriverOrder.setOrderStatus("PENDING");
        noDriverOrder.setOrderDate(LocalDateTime.of(2026, 3, 30, 10, 0));

        when(ordersDao.findById(1)).thenReturn(Optional.of(noDriverOrder));
        when(deliveryDriversRepository.findById(301)).thenReturn(Optional.of(driver));
        when(ordersDao.save(noDriverOrder)).thenReturn(noDriverOrder);

        String result = service.assignDriver(1, 301);

        assertEquals("Driver ID: 301 assigned successfully to Order ID: 1", result);
        assertEquals(301, noDriverOrder.getDeliveryDriver().getDriverId());
    }

    @Test
    void getAllOrdersPaged_returnsPagedResponse() {
        Page<OrdersEntity> page = new PageImpl<>(List.of(orderEntity));
        when(ordersDao.findAllPaged(any(Pageable.class))).thenReturn(page);

        PagedResponse<Orders> result = service.getAllOrdersPaged(0, 10);

        assertEquals(1, result.getItems().size());
        assertEquals(0, result.getCurrentPage());
        assertEquals(1, result.getElementsInCurrentPage());
        verify(ordersDao).findAllPaged(any(Pageable.class));
    }
}
