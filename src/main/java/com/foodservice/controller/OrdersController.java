package com.foodservice.controller;

import com.foodservice.dto.CustomerOrderSummary;
import com.foodservice.dto.Orders;
import com.foodservice.dto.PagedResponse;
import com.foodservice.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apifor/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService ordersService;

    // ─── POST /apifor/orders ──────────────────────────────────────────────────
    // 1. Create a new order (auto ID)
    @PostMapping
    public ResponseEntity<String> createOrder(@Valid @RequestBody Orders dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordersService.createOrder(dto));
    }

    

    // ─── GET /apifor/orders ───────────────────────────────────────────────────
    // 2. Get all orders (optionally filter by status)
    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders(
            @RequestParam(value = "status", required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(ordersService.getOrdersByStatus(status));
        }
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    // 3. Get a single order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> getOrderById(@PathVariable("orderId") Integer orderId) {
        return ResponseEntity.ok(ordersService.getOrderById(orderId));
    }

    // 4. Get all orders for a customer (optionally filter by status)
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Orders>> getOrdersByCustomer(
            @PathVariable Integer customerId,
            @RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(ordersService.getOrdersByCustomerAndStatus(customerId, status));
        }
        return ResponseEntity.ok(ordersService.getOrdersByCustomer(customerId));
    }

    // 5. Get all orders for a restaurant (optionally filter by status)
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Orders>> getOrdersByRestaurant(
            @PathVariable Integer restaurantId,
            @RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(ordersService.getOrdersByRestaurantAndStatus(restaurantId, status));
        }
        return ResponseEntity.ok(ordersService.getOrdersByRestaurant(restaurantId));
    }

    // 6. Get all orders assigned to a specific driver
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Orders>> getOrdersByDriver(@PathVariable Integer driverId) {
        return ResponseEntity.ok(ordersService.getOrdersByDriver(driverId));
    }

    

    

    // 7. Count orders for a restaurant by status
    @GetMapping("/restaurant/{restaurantId}/count")
    public ResponseEntity<Map<String, Long>> countOrdersByRestaurantAndStatus(
            @PathVariable Integer restaurantId,
            @RequestParam String status) {
        Long count = ordersService.countOrdersByRestaurantAndStatus(restaurantId, status);
        return ResponseEntity.ok(Map.of("count", count));
    }

    // ─── PUT /apifor/orders ───────────────────────────────────────────────────
    // 8. Update an order (customer, restaurant, driver, status, date)
    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrder(
            @PathVariable Integer orderId,
            @RequestBody Orders dto) {
        return ResponseEntity.ok(ordersService.updateOrder(orderId, dto));
    }

    // ─── PATCH /apifor/orders ─────────────────────────────────────────────────
    // 9. Update only the order status
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam String status) {
        return ResponseEntity.ok(ordersService.updateOrderStatus(orderId, status));
    }

    // 10. Assign or reassign a delivery driver to an order
    @PatchMapping("/{orderId}/assign-driver")
    public ResponseEntity<String> assignDriver(
            @PathVariable Integer orderId,
            @RequestParam Integer driverId) {
        return ResponseEntity.ok(ordersService.assignDriver(orderId, driverId));
    }

    

    // 11. Get all orders paged
    @GetMapping("/paged")
    public ResponseEntity<PagedResponse<Orders>> getAllOrdersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ordersService.getAllOrdersPaged(page, size));
    }
}