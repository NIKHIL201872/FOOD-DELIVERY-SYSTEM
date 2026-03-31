package com.foodservice.controller;

import com.foodservice.dto.CustomerOrderSummary;
import com.foodservice.dto.Orders;
import com.foodservice.dto.PagedResponse;
import com.foodservice.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrdersControllerTest {

    @Mock
    private OrderService ordersService;

    @InjectMocks
    private OrdersController ordersController;

    private Orders sampleOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleOrder = new Orders(1, 101, 201, 301, "PENDING", LocalDateTime.of(2026, 3, 30, 10, 0));
    }

    @Test
    void createOrder_returnsCreated() {
        when(ordersService.createOrder(sampleOrder)).thenReturn("Order inserted");

        ResponseEntity<String> response = ordersController.createOrder(sampleOrder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Order inserted", response.getBody());
        verify(ordersService).createOrder(sampleOrder);
    }

    @Test
    void createOrderManual_returnsCreated() {
        when(ordersService.createOrderManual(sampleOrder)).thenReturn("Order inserted manual");

        ResponseEntity<String> response = ordersController.createOrderManual(sampleOrder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Order inserted manual", response.getBody());
        verify(ordersService).createOrderManual(sampleOrder);
    }

    @Test
    void getAllOrders_withoutStatus_returnsAllOrders() {
        when(ordersService.getAllOrders()).thenReturn(List.of(sampleOrder));

        ResponseEntity<List<Orders>> response = ordersController.getAllOrders(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(ordersService).getAllOrders();
    }

    @Test
    void getAllOrders_withStatus_returnsFilteredOrders() {
        when(ordersService.getOrdersByStatus("PENDING")).thenReturn(List.of(sampleOrder));

        ResponseEntity<List<Orders>> response = ordersController.getAllOrders("PENDING");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(ordersService).getOrdersByStatus("PENDING");
    }

    @Test
    void getOrderById_returnsOrder() {
        when(ordersService.getOrderById(1)).thenReturn(sampleOrder);

        ResponseEntity<Orders> response = ordersController.getOrderById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getOrderId());
        verify(ordersService).getOrderById(1);
    }

    @Test
    void getOrdersByCustomer_withoutStatus_returnsOrders() {
        when(ordersService.getOrdersByCustomer(101)).thenReturn(List.of(sampleOrder));

        ResponseEntity<List<Orders>> response = ordersController.getOrdersByCustomer(101, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(ordersService).getOrdersByCustomer(101);
    }

    @Test
    void getOrdersByCustomer_withStatus_returnsOrders() {
        when(ordersService.getOrdersByCustomerAndStatus(101, "PENDING")).thenReturn(List.of(sampleOrder));

        ResponseEntity<List<Orders>> response = ordersController.getOrdersByCustomer(101, "PENDING");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(ordersService).getOrdersByCustomerAndStatus(101, "PENDING");
    }

    @Test
    void getOrdersByRestaurant_withoutStatus_returnsOrders() {
        when(ordersService.getOrdersByRestaurant(201)).thenReturn(List.of(sampleOrder));

        ResponseEntity<List<Orders>> response = ordersController.getOrdersByRestaurant(201, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(ordersService).getOrdersByRestaurant(201);
    }

    @Test
    void getOrdersByRestaurant_withStatus_returnsOrders() {
        when(ordersService.getOrdersByRestaurantAndStatus(201, "PENDING")).thenReturn(List.of(sampleOrder));

        ResponseEntity<List<Orders>> response = ordersController.getOrdersByRestaurant(201, "PENDING");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(ordersService).getOrdersByRestaurantAndStatus(201, "PENDING");
    }

    @Test
    void getOrdersByDriver_returnsOrders() {
        when(ordersService.getOrdersByDriver(301)).thenReturn(List.of(sampleOrder));

        ResponseEntity<List<Orders>> response = ordersController.getOrdersByDriver(301);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(ordersService).getOrdersByDriver(301);
    }

    @Test
    void getOrdersByDateRange_returnsOrders() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 31, 23, 59);
        when(ordersService.getOrdersByDateRange(start, end)).thenReturn(List.of(sampleOrder));

        ResponseEntity<List<Orders>> response = ordersController.getOrdersByDateRange(start, end);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(ordersService).getOrdersByDateRange(start, end);
    }

    @Test
    void getOrdersByRestaurantAndDateRange_returnsOrders() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 31, 23, 59);
        when(ordersService.getOrdersByRestaurantAndDateRange(201, start, end))
                .thenReturn(List.of(sampleOrder));

        ResponseEntity<List<Orders>> response =
                ordersController.getOrdersByRestaurantAndDateRange(201, start, end);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(ordersService).getOrdersByRestaurantAndDateRange(201, start, end);
    }

    @Test
    void countOrdersByRestaurantAndStatus_returnsCountMap() {
        when(ordersService.countOrdersByRestaurantAndStatus(201, "PENDING")).thenReturn(7L);

        ResponseEntity<Map<String, Long>> response =
                ordersController.countOrdersByRestaurantAndStatus(201, "PENDING");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(7L, response.getBody().get("count"));
        verify(ordersService).countOrdersByRestaurantAndStatus(201, "PENDING");
    }

    @Test
    void updateOrder_returnsOk() {
        when(ordersService.updateOrder(1, sampleOrder)).thenReturn("Order updated");

        ResponseEntity<String> response = ordersController.updateOrder(1, sampleOrder);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order updated", response.getBody());
        verify(ordersService).updateOrder(1, sampleOrder);
    }

    @Test
    void updateOrderStatus_returnsOk() {
        when(ordersService.updateOrderStatus(1, "OUT_FOR_DELIVERY")).thenReturn("Status updated");

        ResponseEntity<String> response = ordersController.updateOrderStatus(1, "OUT_FOR_DELIVERY");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status updated", response.getBody());
        verify(ordersService).updateOrderStatus(1, "OUT_FOR_DELIVERY");
    }

    @Test
    void assignDriver_returnsOk() {
        when(ordersService.assignDriver(1, 301)).thenReturn("Driver assigned");

        ResponseEntity<String> response = ordersController.assignDriver(1, 301);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Driver assigned", response.getBody());
        verify(ordersService).assignDriver(1, 301);
    }

    @Test
    void cancelOrder_returnsOk() {
        when(ordersService.cancelOrder(1)).thenReturn("Order cancelled");

        ResponseEntity<String> response = ordersController.cancelOrder(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order cancelled", response.getBody());
        verify(ordersService).cancelOrder(1);
    }

    @Test
    void deleteOrder_returnsOkMessage() {
        ResponseEntity<String> response = ordersController.deleteOrder(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order deleted successfully for ID: 1", response.getBody());
        verify(ordersService).deleteOrder(1);
    }

    @Test
    void getPendingUnassignedOrders_returnsOrders() {
        when(ordersService.getPendingUnassignedOrders()).thenReturn(List.of(sampleOrder));

        ResponseEntity<List<Orders>> response = ordersController.getPendingUnassignedOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(ordersService).getPendingUnassignedOrders();
    }

    @Test
    void getActiveOrdersByDriver_returnsOrders() {
        when(ordersService.getActiveOrdersByDriver(301)).thenReturn(List.of(sampleOrder));

        ResponseEntity<List<Orders>> response = ordersController.getActiveOrdersByDriver(301);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(ordersService).getActiveOrdersByDriver(301);
    }

    @Test
    void getRestaurantRevenue_returnsMap() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 31, 23, 59);
        Map<String, Object> revenue = Map.of("restaurantId", 201, "revenue", 1299.0);
        when(ordersService.getRestaurantRevenue(201, start, end)).thenReturn(revenue);

        ResponseEntity<Map<String, Object>> response =
                ordersController.getRestaurantRevenue(201, start, end);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1299.0, response.getBody().get("revenue"));
        verify(ordersService).getRestaurantRevenue(201, start, end);
    }

    @Test
    void getCustomerSummary_returnsSummary() {
        CustomerOrderSummary summary = new CustomerOrderSummary(101, 5L, 201);
        when(ordersService.getCustomerSummary(101)).thenReturn(summary);

        ResponseEntity<CustomerOrderSummary> response = ordersController.getCustomerSummary(101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(101, response.getBody().getCustomerId());
        assertEquals(5L, response.getBody().getTotalOrders());
        verify(ordersService).getCustomerSummary(101);
    }

    @Test
    void getAllOrdersPaged_returnsPagedResponse() {
        PagedResponse<Orders> paged = new PagedResponse<>(List.of(sampleOrder), 0, 1, 1L, 1);
        when(ordersService.getAllOrdersPaged(0, 10)).thenReturn(paged);

        ResponseEntity<PagedResponse<Orders>> response = ordersController.getAllOrdersPaged(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getItems().size());
        verify(ordersService).getAllOrdersPaged(0, 10);
    }
}
