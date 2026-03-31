package com.foodservice.controller;

import com.foodservice.dto.OrderItems;
import com.foodservice.services.OrderItemsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderItemsControllerTest {

    @Mock
    private OrderItemsService service;

    @InjectMocks
    private OrderItemsController controller;

    private OrderItems sampleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleDto = new OrderItems();
        sampleDto.setOrderItemId(1);
        sampleDto.setOrderId(101);
        sampleDto.setItemId(501);
        sampleDto.setQuantity(2);
        sampleDto.setItemName("Burger");
        sampleDto.setItemPrice(new BigDecimal("199.00"));
    }

    @Test
    void create_returnsCreated() {
        when(service.create(sampleDto)).thenReturn("Order item inserted");

        ResponseEntity<String> response = controller.create(sampleDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Order item inserted", response.getBody());
        verify(service).create(sampleDto);
    }

    @Test
    void createWithManualId_returnsCreated() {
        when(service.createWithManualId(sampleDto)).thenReturn("Order item inserted with manual id");

        ResponseEntity<String> response = controller.createWithManualId(sampleDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Order item inserted with manual id", response.getBody());
        verify(service).createWithManualId(sampleDto);
    }

    @Test
    void getAll_returnsOkList() {
        when(service.getAll()).thenReturn(List.of(sampleDto));

        ResponseEntity<List<OrderItems>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service).getAll();
    }

    @Test
    void getById_returnsOkDto() {
        when(service.getById(1)).thenReturn(sampleDto);

        ResponseEntity<OrderItems> response = controller.getById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getOrderItemId());
        verify(service).getById(1);
    }

    @Test
    void update_returnsOkMessage() {
        when(service.update(1, sampleDto)).thenReturn("Order item updated");

        ResponseEntity<String> response = controller.update(1, sampleDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order item updated", response.getBody());
        verify(service).update(1, sampleDto);
    }

    @Test
    void delete_returnsOkMessage() {
        when(service.delete(1)).thenReturn("Order item deleted");

        ResponseEntity<String> response = controller.delete(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order item deleted", response.getBody());
        verify(service).delete(1);
    }

    @Test
    void getByOrderId_returnsOkList() {
        when(service.getByOrderId(101)).thenReturn(List.of(sampleDto));

        ResponseEntity<List<OrderItems>> response = controller.getByOrderId(101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service).getByOrderId(101);
    }

    @Test
    void getByItemId_returnsOkList() {
        when(service.getByItemId(501)).thenReturn(List.of(sampleDto));

        ResponseEntity<List<OrderItems>> response = controller.getByItemId(501);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service).getByItemId(501);
    }

    @Test
    void getByOrderAndItem_returnsOkList() {
        when(service.getByOrderAndItem(101, 501)).thenReturn(List.of(sampleDto));

        ResponseEntity<List<OrderItems>> response = controller.getByOrderAndItem(101, 501);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service).getByOrderAndItem(101, 501);
    }

    @Test
    void getTotalQuantityByOrder_returnsOkValue() {
        when(service.getTotalQuantityByOrder(101)).thenReturn(5);

        ResponseEntity<Integer> response = controller.getTotalQuantityByOrder(101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody());
        verify(service).getTotalQuantityByOrder(101);
    }

    @Test
    void getTotalPriceByOrder_returnsOkValue() {
        when(service.getTotalPriceByOrder(101)).thenReturn(398.00);

        ResponseEntity<Double> response = controller.getTotalPriceByOrder(101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(398.00, response.getBody());
        verify(service).getTotalPriceByOrder(101);
    }

    @Test
    void getMostOrderedItems_returnsOkList() {
        when(service.getMostOrderedItems()).thenReturn(List.of(sampleDto));

        ResponseEntity<List<OrderItems>> response = controller.getMostOrderedItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service).getMostOrderedItems();
    }

    @Test
    void createBulk_returnsCreated() {
        List<OrderItems> dtos = List.of(sampleDto);
        when(service.createBulk(dtos)).thenReturn("1 order item(s) inserted successfully");

        ResponseEntity<String> response = controller.createBulk(dtos);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("1 order item(s) inserted successfully", response.getBody());
        verify(service).createBulk(dtos);
    }

    @Test
    void deleteByOrderId_returnsOkMessage() {
        when(service.deleteByOrderId(101)).thenReturn("All order items deleted");

        ResponseEntity<String> response = controller.deleteByOrderId(101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("All order items deleted", response.getBody());
        verify(service).deleteByOrderId(101);
    }

    @Test
    void updateQuantity_returnsOkMessage() {
        when(service.updateQuantity(1, 3)).thenReturn("Quantity updated");

        ResponseEntity<String> response = controller.updateQuantity(1, 3);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Quantity updated", response.getBody());
        verify(service).updateQuantity(1, 3);
    }

    @Test
    void getOrderItemDetails_returnsOkList() {
        when(service.getOrderItemDetails(101)).thenReturn(List.of(sampleDto));

        ResponseEntity<List<OrderItems>> response = controller.getOrderItemDetails(101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service).getOrderItemDetails(101);
    }

    @Test
    void getMenuItemByOrderItemId_returnsOkDto() {
        when(service.getMenuItemByOrderItemId(1)).thenReturn(sampleDto);

        ResponseEntity<OrderItems> response = controller.getMenuItemByOrderItemId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getOrderItemId());
        verify(service).getMenuItemByOrderItemId(1);
    }
}
