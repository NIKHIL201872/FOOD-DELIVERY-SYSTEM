package com.foodservice.controller;

import com.foodservice.dto.OrderItems;
import com.foodservice.services.OrderItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderitems")
public class OrderItemsController {

    @Autowired
    private OrderItemsService service;

    // ─────────────────────────────────────────
    //  1. CREATE
    // ─────────────────────────────────────────
    @PostMapping
    public ResponseEntity<String> create(@RequestBody OrderItems dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    
    // ─────────────────────────────────────────
    //  2. GET ALL
    // ─────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<OrderItems>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ─────────────────────────────────────────
    //  3. GET BY ID
    // ─────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<OrderItems> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ─────────────────────────────────────────
    //  4. UPDATE BY ID
    // ─────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody OrderItems dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    

    // ─────────────────────────────────────────
    //  5. GET BY ORDER ID
    // ─────────────────────────────────────────
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItems>> getByOrderId(@PathVariable("orderId") Integer orderId) {
        return ResponseEntity.ok(service.getByOrderId(orderId));
    }

    // ─────────────────────────────────────────
    //  6. GET BY ITEM ID
    // ─────────────────────────────────────────
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<OrderItems>> getByItemId(@PathVariable("itemId") Integer itemId) {
        return ResponseEntity.ok(service.getByItemId(itemId));
    }

    // ─────────────────────────────────────────
    //  7. GET BY ORDER ID + ITEM ID
    // ─────────────────────────────────────────
    @GetMapping("/order/{orderId}/item/{itemId}")
    public ResponseEntity<List<OrderItems>> getByOrderAndItem(
            @PathVariable("orderId") Integer orderId,
            @PathVariable("itemId") Integer itemId) {
        return ResponseEntity.ok(service.getByOrderAndItem(orderId, itemId));
    }

    // ─────────────────────────────────────────
    //  8. GET TOTAL QUANTITY BY ORDER ID
    // ─────────────────────────────────────────
    @GetMapping("/order/{orderId}/total-quantity")
    public ResponseEntity<Integer> getTotalQuantityByOrder(@PathVariable("orderId") Integer orderId) {
        return ResponseEntity.ok(service.getTotalQuantityByOrder(orderId));
    }

    // ─────────────────────────────────────────
    //  9. GET TOTAL PRICE BY ORDER ID
    // ─────────────────────────────────────────
    @GetMapping("/order/{orderId}/total-price")
    public ResponseEntity<Double> getTotalPriceByOrder(@PathVariable("orderId") Integer orderId) {
        return ResponseEntity.ok(service.getTotalPriceByOrder(orderId));
    }

    // ─────────────────────────────────────────
    //  10. GET MOST ORDERED ITEMS
    // ─────────────────────────────────────────
    @GetMapping("/most-ordered")
    public ResponseEntity<List<OrderItems>> getMostOrderedItems() {
        return ResponseEntity.ok(service.getMostOrderedItems());
    }

    

    

    // ─────────────────────────────────────────
    //  11. UPDATE QUANTITY ONLY (PATCH)
    // ─────────────────────────────────────────
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<String> updateQuantity(
            @PathVariable("id") Integer id,
            @RequestParam("quantity") Integer quantity) {
        return ResponseEntity.ok(service.updateQuantity(id, quantity));
    }

    // ─────────────────────────────────────────
    //  12. GET ENRICHED ORDER ITEM DETAILS
    // ─────────────────────────────────────────
    @GetMapping("/order/{orderId}/details")
    public ResponseEntity<List<OrderItems>> getOrderItemDetails(
            @PathVariable("orderId") Integer orderId) {
        return ResponseEntity.ok(service.getOrderItemDetails(orderId));
    }
	 // ─────────────────────────────────────────
	//  13. GET MENU ITEM BY ORDER ITEM ID
	// ─────────────────────────────────────────
	@GetMapping("/{id}/menuitem")
	public ResponseEntity<OrderItems> getMenuItemByOrderItemId(@PathVariable("id") Integer id) {
	    return ResponseEntity.ok(service.getMenuItemByOrderItemId(id));
	}
}