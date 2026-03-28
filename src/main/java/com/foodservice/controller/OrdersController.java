package com.foodservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodservice.dto.ApiResponse;
import com.foodservice.dto.Orders;
import com.foodservice.services.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService service;
   // private final OrderItemsService orderItemsService; // ✅ ADD THIS

    @PostMapping
    public ResponseEntity<ApiResponse<Orders>> create(@Valid @RequestBody Orders dto) {
        return ResponseEntity.ok(new ApiResponse<>("Order Created", service.createOrder(dto)));
    }

    @GetMapping("/{id}")
    public Orders getOrder(@PathVariable Integer id) {
        return service.getOrder(id);
    }

    @GetMapping("/{id}/full")
    public Map<String, Object> getFull(@PathVariable Integer id) {
        return service.getFullOrderDetails(id);
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable Integer id,
                             @RequestParam String status) {
        service.updateStatus(id, status);
    }

    @PatchMapping("/{id}/assign-driver")
    public void assignDriver(@PathVariable Integer id,
                             @RequestParam Integer driverId) {
        service.assignDriver(id, driverId);
    }

//    @GetMapping("/{id}/items")
//    public List<OrderItemsEntity> getItems(@PathVariable Integer id) {
//        return orderItemsService.getByOrder(id);
//    }
}