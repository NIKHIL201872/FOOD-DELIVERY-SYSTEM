package com.foodservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.foodservice.dto.OrderCoupons;
import com.foodservice.entity.OrderCouponsEntity;
import com.foodservice.services.CouponsService;
import com.foodservice.services.OrderCouponsService;

@RestController
@RequestMapping("/api/ordercoupons")
@CrossOrigin("*")
public class OrderCouponsController {

    @Autowired
    private OrderCouponsService service;

    /*@PostMapping("/{orderId}/apply-coupon")
    public String applyCoupon(@PathVariable ("orderId") Integer orderId,
                              @RequestParam ("couponId") Integer couponId) {
        return service.applyCoupon(orderId, couponId);
    }
    */


    @GetMapping("/{orderId}/coupons")
    public List<Object> getCoupons(@PathVariable ("orderId") Integer orderId) {
        return service.getCouponsByOrder(orderId);
    }

    @GetMapping("/coupon/{couponId}")
    public List<Object> getByCoupon(@PathVariable ("couponId") Integer couponId) {
        return service.getByCouponId(couponId);
    }

    @GetMapping("/order/{orderId}/coupon/{couponId}")
    public List<Object> getByOrderAndCoupon(
            @PathVariable ("orderId") Integer orderId,
            @PathVariable ("couponId") Integer couponId) {

        return service.getByOrderAndCoupon(orderId, couponId);
    }
    @GetMapping
    public List<Object> getAll() {
        return service.getAll();
    }
}