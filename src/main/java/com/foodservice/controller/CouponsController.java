package com.foodservice.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.foodservice.dto.Coupons;
import com.foodservice.entity.CouponsEntity;
import com.foodservice.services.CouponsService;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin("*")
public class CouponsController {

    @Autowired
    private CouponsService service;

    @PostMapping
    public Object create(@RequestBody Coupons dto) {
        return service.createCoupon(dto);
    }

    @GetMapping
    public List<CouponsEntity> getAll() {
        return service.getAllCoupons();
    }

    @GetMapping("/{id}")
    public CouponsEntity get(@PathVariable ("id") Integer id) {
        return service.getCoupon(id);
    }
    
    @GetMapping("/by-code")
    public List<CouponsEntity> getByCode(@RequestParam ("code") String code) {
        return service.getByCouponCode(code);
    }
        
    @GetMapping("/by-discount")
    public List<CouponsEntity> getByDiscount(@RequestParam ("discount") BigDecimal discount) {
        return service.getByDiscount(discount);
    }
    
    @GetMapping("/by-expiry")
    public List<CouponsEntity> getByExpiry(@RequestParam ("date") String date) {
        return service.getByExpiry(LocalDate.parse(date));
    }
    
    @GetMapping("/by-code-discount")
    public List<CouponsEntity> getByCodeDiscount1(
            @RequestParam ("code") String code,
            @RequestParam ("discount") BigDecimal discount) {

        return service.getByCodeAndDiscount(code, discount);
    }

    @GetMapping("/by-code-expiry")
    public List<CouponsEntity> getByCodeExpiry1(
            @RequestParam ("code") String code,
            @RequestParam ("date") String date) {

        return service.getByCodeAndExpiry(code, LocalDate.parse(date));
    }

    @GetMapping("/by-discount-expiry")
    public List<CouponsEntity> getByDiscountExpiry(
            @RequestParam ("discount") BigDecimal discount,
            @RequestParam ("date") String date) {

        return service.getByDiscountAndExpiry(discount, LocalDate.parse(date));
    }
    
    @GetMapping("/by-code-starts")
    public List<CouponsEntity> getByCodeStarts(@RequestParam ("prefix") String prefix) {
        return service.getByCodeStartsWith(prefix);
    }
    
    @GetMapping("/by-min-discount")
    public List<CouponsEntity> getByMinDiscount(@RequestParam ("min") BigDecimal min) {
        return service.getByMinDiscount(min);
    }
    
    @GetMapping("/by-max-discount")
    public List<CouponsEntity> getByMaxDiscount(@RequestParam ("max") BigDecimal max) {
        return service.getByMaxDiscount(max);
    }
    
    @GetMapping("/by-discount-range")
    public List<CouponsEntity> getByDiscountRange(
            @RequestParam ("min") BigDecimal min,
            @RequestParam ("max") BigDecimal max) {

        return service.getByDiscountRange(min, max);
    }
    
    @PutMapping("/{id}/discount")
    public String updateDiscount(
            @PathVariable ("id") Integer id,
            @RequestParam ("discount") BigDecimal discount) {

        return service.updateDiscount(id, discount);
    }
    
    @PutMapping("/{id}/expiry")
    public String updateExpiry(
            @PathVariable ("id") Integer id,
            @RequestParam ("date") String date) {

        return service.updateExpiry(id, java.time.LocalDate.parse(date));
    }
    
    @PutMapping("/{id}")
    public String updateCoupon(
            @PathVariable ("id") Integer id,
            @RequestParam ("discount") BigDecimal discount,
            @RequestParam ("date") String date) {

        return service.updateCoupon(id, discount, java.time.LocalDate.parse(date));
    }

    @GetMapping("/validate/{id}")
    public String validate(@PathVariable ("id") Integer id) {
        return service.validateCoupon(id);
    }

    @GetMapping("/active")
    public List<CouponsEntity> active() {
        return service.getActiveCoupons();
    }

    @GetMapping("/expired")
    public List<CouponsEntity> expired() {
        return service.getExpiredCoupons();
    }
}