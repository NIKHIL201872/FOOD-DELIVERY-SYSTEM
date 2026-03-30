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

    // 2. Create order with manually provided ID
    @PostMapping("/manual")
    public ResponseEntity<String> createOrderManual(@Valid @RequestBody Orders dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordersService.createOrderManual(dto));
    }

    // ─── GET /apifor/orders ───────────────────────────────────────────────────
    // 3. Get all orders (optionally filter by status)
    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders(
            @RequestParam(value = "status",required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(ordersService.getOrdersByStatus(status));
        }
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    // 4. Get a single order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> getOrderById(@PathVariable("orderId") Integer orderId) {
        return ResponseEntity.ok(ordersService.getOrderById(orderId));
    }

    // 5. Get all orders for a customer (optionally filter by status)
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Orders>> getOrdersByCustomer(
            @PathVariable Integer customerId,
            @RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(ordersService.getOrdersByCustomerAndStatus(customerId, status));
        }
        return ResponseEntity.ok(ordersService.getOrdersByCustomer(customerId));
    }

    // 6. Get all orders for a restaurant (optionally filter by status)
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Orders>> getOrdersByRestaurant(
            @PathVariable Integer restaurantId,
            @RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(ordersService.getOrdersByRestaurantAndStatus(restaurantId, status));
        }
        return ResponseEntity.ok(ordersService.getOrdersByRestaurant(restaurantId));
    }

    // 7. Get all orders assigned to a specific driver
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Orders>> getOrdersByDriver(@PathVariable Integer driverId) {
        return ResponseEntity.ok(ordersService.getOrdersByDriver(driverId));
    }

    // 8. Get orders within a date range
    @GetMapping("/date-range")
    public ResponseEntity<List<Orders>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(ordersService.getOrdersByDateRange(startDate, endDate));
    }

    // 9. Get orders for a restaurant within a date range
    @GetMapping("/restaurant/{restaurantId}/date-range")
    public ResponseEntity<List<Orders>> getOrdersByRestaurantAndDateRange(
            @PathVariable Integer restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(ordersService.getOrdersByRestaurantAndDateRange(restaurantId, startDate, endDate));
    }

    // 10. Count orders for a restaurant by status
    @GetMapping("/restaurant/{restaurantId}/count")
    public ResponseEntity<Map<String, Long>> countOrdersByRestaurantAndStatus(
            @PathVariable Integer restaurantId,
            @RequestParam String status) {
        Long count = ordersService.countOrdersByRestaurantAndStatus(restaurantId, status);
        return ResponseEntity.ok(Map.of("count", count));
    }

    // ─── PUT /apifor/orders ───────────────────────────────────────────────────
    // 11. Update an order (customer, restaurant, driver, status, date)
    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrder(
            @PathVariable Integer orderId,
            @RequestBody Orders dto) {
        return ResponseEntity.ok(ordersService.updateOrder(orderId, dto));
    }

    // ─── PATCH /apifor/orders ─────────────────────────────────────────────────
    // 12. Update only the order status
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam String status) {
        return ResponseEntity.ok(ordersService.updateOrderStatus(orderId, status));
    }

    // 13. Assign or reassign a delivery driver to an order
    @PatchMapping("/{orderId}/assign-driver")
    public ResponseEntity<String> assignDriver(
            @PathVariable Integer orderId,
            @RequestParam Integer driverId) {
        return ResponseEntity.ok(ordersService.assignDriver(orderId, driverId));
    }

    // 14. Cancel an order (blocked if already DELIVERED or CANCELLED)
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(ordersService.cancelOrder(orderId));
    }

    // ─── DELETE /apifor/orders ────────────────────────────────────────────────
    // 15. Delete an order by ID
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer orderId) {
        ordersService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted successfully for ID: " + orderId);
    }

    // ─── NEW ENDPOINTS ────────────────────────────────────────────────────────
    // 16. All PENDING orders with no driver assigned yet
    @GetMapping("/pending-unassigned")
    public ResponseEntity<List<Orders>> getPendingUnassignedOrders() {
        return ResponseEntity.ok(ordersService.getPendingUnassignedOrders());
    }

    // 17. Orders currently OUT_FOR_DELIVERY for a driver
    @GetMapping("/driver/{driverId}/active")
    public ResponseEntity<List<Orders>> getActiveOrdersByDriver(@PathVariable Integer driverId) {
        return ResponseEntity.ok(ordersService.getActiveOrdersByDriver(driverId));
    }

    // 18. Total revenue for a restaurant in a date range
    @GetMapping("/restaurant/{restaurantId}/revenue")
    public ResponseEntity<Map<String, Object>> getRestaurantRevenue(
            @PathVariable Integer restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(ordersService.getRestaurantRevenue(restaurantId, startDate, endDate));
    }

    // 19. Total order count + top restaurant for a customer
    @GetMapping("/customer/{customerId}/summary")
    public ResponseEntity<CustomerOrderSummary> getCustomerSummary(@PathVariable Integer customerId) {
        return ResponseEntity.ok(ordersService.getCustomerSummary(customerId));
    }

    // 20. Get all orders paged
    @GetMapping("/paged")
    public ResponseEntity<PagedResponse<Orders>> getAllOrdersPaged(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ordersService.getAllOrdersPaged(page, size));
    }
}