package com.foodservice.controller;

import com.foodservice.dto.OrderItems;
import com.foodservice.services.OrderItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderitems")
public class OrderItemsController {

    @Autowired
    private OrderItemsService service;

    @PostMapping
    public ResponseEntity<OrderItems> create(@RequestBody OrderItems dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<OrderItems>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItems> getById(@PathVariable ("id")Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItems> update(@PathVariable ("id") Integer id,
                                             @RequestBody OrderItems dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable ("id")Integer id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItems>> getByOrderId(@PathVariable ("orderId")Integer orderId) {
        return ResponseEntity.ok(service.getByOrderId(orderId));
    }
}