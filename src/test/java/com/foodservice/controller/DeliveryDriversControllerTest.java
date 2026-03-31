package com.foodservice.controller;

import com.foodservice.dto.ApiResponse;
import com.foodservice.dto.DeliveryDrivers;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.services.DeliveryDriversService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeliveryDriversControllerTest {

    @Mock
    private DeliveryDriversService service;

    @InjectMocks
    private DeliveryDriversController controller;

    private DeliveryDrivers sampleDriver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleDriver = new DeliveryDrivers(1, "Rahul", "9999999999", "Bike");
    }

    @Test
    void create_returnsCreatedResponse() {
        when(service.createDriver(sampleDriver)).thenReturn("Driver inserted successfully with ID: 1");

        ResponseEntity<ApiResponse<String>> response = controller.create(sampleDriver);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Driver Created", response.getBody().getMessage());
        assertEquals("Driver inserted successfully with ID: 1", response.getBody().getData());
        verify(service).createDriver(sampleDriver);
    }

    @Test
    void getDriver_returnsDriver() {
        when(service.getDriver(1)).thenReturn(sampleDriver);

        DeliveryDrivers response = controller.getDriver(1);

        assertEquals(1, response.getDriverId());
        assertEquals("Rahul", response.getDriverName());
        verify(service).getDriver(1);
    }

    @Test
    void getAllDrivers_returnsList() {
        when(service.getAllDrivers()).thenReturn(List.of(sampleDriver));

        List<DeliveryDrivers> response = controller.getAllDrivers();

        assertEquals(1, response.size());
        assertEquals(1, response.get(0).getDriverId());
        verify(service).getAllDrivers();
    }

    @Test
    void update_returnsUpdatedDriver() {
        DeliveryDrivers updated = new DeliveryDrivers(1, "Rahul Kumar", "8888888888", "Scooter");
        when(service.updateDriver(1, sampleDriver)).thenReturn(updated);

        DeliveryDrivers response = controller.update(1, sampleDriver);

        assertEquals("Rahul Kumar", response.getDriverName());
        assertEquals("Scooter", response.getDriverVehicle());
        verify(service).updateDriver(1, sampleDriver);
    }

    @Test
    void delete_callsService() {
        doNothing().when(service).deleteDriver(1);

        controller.delete(1);

        verify(service).deleteDriver(1);
    }

    @Test
    void assignOrder_returnsOkMessage() {
        doNothing().when(service).assignDriverToOrder(1, 101);

        ResponseEntity<String> response = controller.assignOrder(1, 101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Driver Assigned", response.getBody());
        verify(service).assignDriverToOrder(1, 101);
    }

    @Test
    void getDriverOrders_returnsOrders() {
        OrdersEntity order = new OrdersEntity();
        order.setOrderId(101);
        when(service.getOrdersByDriver(1)).thenReturn(List.of(order));

        List<OrdersEntity> response = controller.getDriverOrders(1);

        assertEquals(1, response.size());
        assertEquals(101, response.get(0).getOrderId());
        verify(service).getOrdersByDriver(1);
    }

    @Test
    void completeOrder_returnsOkMessage() {
        doNothing().when(service).completeDelivery(1, 101);

        ResponseEntity<String> response = controller.completeOrder(1, 101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order Delivered", response.getBody());
        verify(service).completeDelivery(1, 101);
    }
}
