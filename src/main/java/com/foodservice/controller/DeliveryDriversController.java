package com.foodservice.controller;

import com.foodservice.dto.ApiResponse;
import com.foodservice.dto.DeliveryDrivers;
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
    public ResponseEntity<ApiResponse<DeliveryDrivers>> create(@RequestBody DeliveryDrivers dto) {
        return ResponseEntity.ok(
                new ApiResponse<>("Driver Created", service.createDriver(dto))
        );
    }

    // GET ONE
    @GetMapping("/{id}")
    public DeliveryDrivers getDriver(@PathVariable Integer id) {
        return service.getDriver(id);
    }

    // GET ALL
    @GetMapping
    public List<DeliveryDrivers> getAllDrivers() {
        return service.getAllDrivers();
    }

    // UPDATE
    @PutMapping("/{id}")
    public DeliveryDrivers update(@PathVariable Integer id,
                                  @RequestBody DeliveryDrivers dto) {
        return service.updateDriver(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.deleteDriver(id);
    }
}