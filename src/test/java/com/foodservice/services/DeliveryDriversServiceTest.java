package com.foodservice.services;

import com.foodservice.DAO.DeliveryDriversDAO;
import com.foodservice.dto.DeliveryDrivers;
import com.foodservice.entity.DeliveryDriversEntity;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.exception.BadRequestException;
import com.foodservice.exception.ResourceConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeliveryDriversServiceTest {

    @Mock
    private DeliveryDriversDAO driverDAO;

    @InjectMocks
    private DeliveryDriversService service;

    private DeliveryDrivers dto;
    private DeliveryDriversEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new DeliveryDrivers(1, "Rahul", "9999999999", "Bike");

        entity = new DeliveryDriversEntity();
        entity.setDriverId(1);
        entity.setDriverName("Rahul");
        entity.setDriverPhone("9999999999");
        entity.setDriverVehicle("Bike");
    }

    @Test
    void createDriver_returnsSuccessMessage() {
        when(driverDAO.existsById(1)).thenReturn(false);
        when(driverDAO.save(org.mockito.ArgumentMatchers.any(DeliveryDriversEntity.class))).thenReturn(entity);

        String result = service.createDriver(dto);

        assertEquals("Driver inserted successfully with ID: 1", result);
        verify(driverDAO).save(org.mockito.ArgumentMatchers.any(DeliveryDriversEntity.class));
    }

    @Test
    void createDriver_throwsWhenDtoNull() {
        assertThrows(BadRequestException.class, () -> service.createDriver(null));
        verify(driverDAO, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void createDriver_throwsWhenIdAlreadyExists() {
        when(driverDAO.existsById(1)).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> service.createDriver(dto));
        verify(driverDAO, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void getDriver_returnsDto() {
        when(driverDAO.findById(1)).thenReturn(entity);

        DeliveryDrivers result = service.getDriver(1);

        assertEquals(1, result.getDriverId());
        assertEquals("Rahul", result.getDriverName());
        assertEquals("Bike", result.getDriverVehicle());
    }

    @Test
    void getAllDrivers_returnsList() {
        when(driverDAO.findAll()).thenReturn(List.of(entity));

        List<DeliveryDrivers> result = service.getAllDrivers();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getDriverId());
    }

    @Test
    void updateDriver_updatesAndReturnsDto() {
        DeliveryDrivers updateDto = new DeliveryDrivers(1, "Rahul Kumar", "8888888888", "Scooter");
        when(driverDAO.findById(1)).thenReturn(entity);
        when(driverDAO.save(entity)).thenReturn(entity);

        DeliveryDrivers result = service.updateDriver(1, updateDto);

        assertEquals("Rahul Kumar", result.getDriverName());
        assertEquals("8888888888", result.getDriverPhone());
        assertEquals("Scooter", result.getDriverVehicle());
    }

    @Test
    void deleteDriver_callsDaoDelete() {
        doNothing().when(driverDAO).delete(1);

        service.deleteDriver(1);

        verify(driverDAO).delete(1);
    }

    @Test
    void assignDriverToOrder_callsDao() {
        doNothing().when(driverDAO).assignDriverToOrder(1, 101);

        service.assignDriverToOrder(1, 101);

        verify(driverDAO).assignDriverToOrder(1, 101);
    }

    @Test
    void getOrdersByDriver_returnsOrdersList() {
        OrdersEntity order = new OrdersEntity();
        order.setOrderId(101);
        when(driverDAO.getOrdersByDriver(1)).thenReturn(List.of(order));

        List<OrdersEntity> result = service.getOrdersByDriver(1);

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getOrderId());
    }

    @Test
    void completeDelivery_callsDao() {
        doNothing().when(driverDAO).completeDelivery(1, 101);

        service.completeDelivery(1, 101);

        verify(driverDAO).completeDelivery(1, 101);
    }
}
