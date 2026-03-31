package com.foodservice.services;

import com.foodservice.DAO.OrderItemsDAO;
import com.foodservice.dto.OrderItems;
import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.entity.OrderItemsEntity;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.exception.OrderItemNotFoundException;
import com.foodservice.exception.ResourceConflictException;
import com.foodservice.exception.ResourceNotFoundException;
import com.foodservice.repository.MenuItemsRepository;
import com.foodservice.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderItemsServiceTest {

    @Mock
    private OrderItemsDAO dao;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private MenuItemsRepository menuItemsRepository;

    @InjectMocks
    private OrderItemsService service;

    private OrderItems dto;
    private OrdersEntity order;
    private MenuItemsEntity item;
    private OrderItemsEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new OrderItems();
        dto.setOrderItemId(1);
        dto.setOrderId(101);
        dto.setItemId(501);
        dto.setQuantity(2);

        order = new OrdersEntity();
        order.setOrderId(101);

        RestaurantsEntity restaurant = new RestaurantsEntity();
        restaurant.setRestaurantId(201);

        item = new MenuItemsEntity();
        item.setItemId(501);
        item.setItemName("Burger");
        item.setItemDescription("Cheese Burger");
        item.setItemPrice(new BigDecimal("199.00"));
        item.setRestaurant(restaurant);

        entity = new OrderItemsEntity();
        entity.setOrderItemId(1);
        entity.setOrder(order);
        entity.setItem(item);
        entity.setQuantity(2);
    }

    @Test
    void create_insertsOrderItemWithGeneratedId() {
        when(ordersRepository.findById(101)).thenReturn(Optional.of(order));
        when(menuItemsRepository.findById(501)).thenReturn(Optional.of(item));
        when(dao.getMaxId()).thenReturn(10);
        when(dao.save(any(OrderItemsEntity.class))).thenReturn(entity);

        String result = service.create(dto);

        assertEquals("Order item inserted successfully with ID: 11", result);
        verify(dao).save(any(OrderItemsEntity.class));
    }

    @Test
    void createWithManualId_throwsConflictWhenIdAlreadyExists() {
        when(dao.findById(1)).thenReturn(Optional.of(entity));

        assertThrows(ResourceConflictException.class, () -> service.createWithManualId(dto));
        verify(dao, never()).save(any(OrderItemsEntity.class));
    }

    @Test
    void getById_returnsDto() {
        when(dao.findById(1)).thenReturn(Optional.of(entity));

        OrderItems result = service.getById(1);

        assertEquals(1, result.getOrderItemId());
        assertEquals(101, result.getOrderId());
        assertEquals(501, result.getItemId());
        assertEquals(2, result.getQuantity());
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(dao.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99));
    }

    @Test
    void getTotalQuantityByOrder_returnsSum() {
        OrderItemsEntity e2 = new OrderItemsEntity();
        e2.setOrderItemId(2);
        e2.setOrder(order);
        e2.setItem(item);
        e2.setQuantity(3);

        when(ordersRepository.findById(101)).thenReturn(Optional.of(order));
        when(dao.findByOrderId(101)).thenReturn(List.of(entity, e2));

        Integer result = service.getTotalQuantityByOrder(101);

        assertEquals(5, result);
    }

    @Test
    void getTotalPriceByOrder_returnsCalculatedTotal() {
        OrderItemsEntity e2 = new OrderItemsEntity();
        e2.setOrderItemId(2);
        e2.setOrder(order);
        e2.setItem(item);
        e2.setQuantity(3);

        when(ordersRepository.findById(101)).thenReturn(Optional.of(order));
        when(dao.findByOrderId(101)).thenReturn(List.of(entity, e2));

        Double result = service.getTotalPriceByOrder(101);

        assertEquals(995.0, result);
    }

    @Test
    void updateQuantity_throwsForInvalidQuantity() {
        assertThrows(OrderItemNotFoundException.class, () -> service.updateQuantity(1, 0));
        verify(dao, never()).save(any(OrderItemsEntity.class));
    }

    @Test
    void updateQuantity_updatesEntity() {
        when(dao.findById(1)).thenReturn(Optional.of(entity));
        when(dao.save(entity)).thenReturn(entity);

        String result = service.updateQuantity(1, 4);

        assertEquals("Order item quantity updated successfully for ID: 1", result);
        assertEquals(4, entity.getQuantity());
        verify(dao).save(entity);
    }

    @Test
    void deleteByOrderId_deletesAllWhenItemsExist() {
        when(ordersRepository.findById(101)).thenReturn(Optional.of(order));
        when(dao.findByOrderId(101)).thenReturn(List.of(entity));
        doNothing().when(dao).deleteAllByOrderId(101);

        String result = service.deleteByOrderId(101);

        assertEquals("All order items deleted successfully for Order ID: 101", result);
        verify(dao).deleteAllByOrderId(101);
    }

    @Test
    void getByOrderAndItem_throwsWhenNoData() {
        when(dao.findByOrderIdAndItemId(101, 501)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> service.getByOrderAndItem(101, 501));
    }
}
