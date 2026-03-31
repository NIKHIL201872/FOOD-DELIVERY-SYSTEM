package com.foodservice.controller;

import com.foodservice.dto.ApiResponse;
import com.foodservice.dto.DeliveryDrivers;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.services.DeliveryDriversService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DeliveryDriversController {

    private final DeliveryDriversService service;

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody DeliveryDrivers dto) {
        return ResponseEntity.status(201).body(
                new ApiResponse<>("Driver Created", service.createDriver(dto))
        );
    }

    // GET ONE
    @GetMapping("/{id}")
    public DeliveryDrivers getDriver(@PathVariable("id") Integer id) {
        return service.getDriver(id);
    }

    // GET ALL
    @GetMapping
    public List<DeliveryDrivers> getAllDrivers() {
        return service.getAllDrivers();
    }

    // UPDATE
    @PutMapping("/{id}")
    public DeliveryDrivers update(@PathVariable("id") Integer id,
                                  @RequestBody DeliveryDrivers dto) {
        return service.updateDriver(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        service.deleteDriver(id);
    }
 // 🔥 Assign Driver to Order
    @PutMapping("/{driverId}/assign-order/{orderId}")
    public ResponseEntity<String> assignOrder(@PathVariable("driverId") Integer driverId,
                                              @PathVariable("orderId") Integer orderId) {
        service.assignDriverToOrder(driverId, orderId);
        return ResponseEntity.ok("Driver Assigned");
    }

    // 🔥 Get Orders of Driver
    @GetMapping("/{driverId}/orders")
    public List<OrdersEntity> getDriverOrders(@PathVariable("driverId") Integer driverId) {
        return service.getOrdersByDriver(driverId);
    }

    // 🔥 Complete Delivery
    @PutMapping("/{driverId}/complete-order/{orderId}")
    public ResponseEntity<String> completeOrder(@PathVariable("driverId") Integer driverId,
                                                @PathVariable("orderId") Integer orderId) {
        service.completeDelivery(driverId, orderId);
        return ResponseEntity.ok("Order Delivered");
    }
}