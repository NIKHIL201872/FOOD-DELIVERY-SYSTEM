package com.foodservice.services;

import java.time.LocalDate;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodservice.DAO.CouponsDAO;
import com.foodservice.DAO.OrderCouponsDAO;
import com.foodservice.dto.Coupons;
import com.foodservice.dto.OrderCoupons;
import com.foodservice.entity.*;
import com.foodservice.exception.*;

@Service
public class OrderCouponsService {

    @Autowired
    private OrderCouponsDAO orderCouponsDAO;

    @Autowired
    private CouponsDAO couponsDAO;

    public String applyCoupon(Integer orderId, Integer couponId) {

        OrdersEntity order = orderCouponsDAO.findOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        CouponsEntity coupon;
        try {
            coupon = couponsDAO.findById(couponId);
        } catch (Exception e) {
            throw new CouponNotFoundException("Coupon not found");
        }
        if (coupon.getExpiryDate() != null &&
                coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CouponExpiredException("Coupon expired");
        }

        boolean alreadyApplied = orderCouponsDAO
                .findByOrderId(orderId)
                .stream()
                .anyMatch(oc -> oc.getCoupon().getCouponId().equals(couponId));

        if (alreadyApplied) {
            throw new RuntimeException("Coupon already applied to this order");
        }

        orderCouponsDAO.save(new OrderCouponsEntity(order, coupon));

        return "Coupon Applied Successfully";
    }
    

    public List<Object> getCouponsByOrder(Integer orderId) {

        List<OrderCouponsEntity> list = orderCouponsDAO.findByOrderId(orderId);

        if (list == null || list.isEmpty()) {
            throw new CouponNotFoundException("No coupons found for this order");
        }

        List<Object> response = new java.util.ArrayList<>();

        for (OrderCouponsEntity oc : list) {

            com.foodservice.dto.OrderCoupons orderDTO =
                    new com.foodservice.dto.OrderCoupons(
                            oc.getOrder().getOrderId(),
                            oc.getCoupon().getCouponId()
                    );
            com.foodservice.dto.Coupons couponDTO =
                    new com.foodservice.dto.Coupons(
                            oc.getCoupon().getCouponId(),
                            oc.getCoupon().getCouponCode(),
                            oc.getCoupon().getDiscountAmount(),
                            oc.getCoupon().getExpiryDate()
                    );

            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("order", orderDTO);
            map.put("coupon", couponDTO);

            response.add(map);
        }

        return response;
    }
    
    public List<Object> getByCouponId(Integer couponId) {

        List<OrderCouponsEntity> list = orderCouponsDAO.findByCouponId(couponId);

        if (list == null || list.isEmpty()) {
            throw new NoRecordsFoundException("No orders found for this coupon");
        }

        List<Object> response = new java.util.ArrayList<>();

        for (OrderCouponsEntity oc : list) {

            OrderCoupons orderDTO = new OrderCoupons(
                    oc.getOrder().getOrderId(),
                    oc.getCoupon().getCouponId()
            );

            Coupons couponDTO = new Coupons(
                    oc.getCoupon().getCouponId(),
                    oc.getCoupon().getCouponCode(),
                    oc.getCoupon().getDiscountAmount(),
                    oc.getCoupon().getExpiryDate()
            );

            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("order", orderDTO);
            map.put("coupon", couponDTO);

            response.add(map);
        }

        return response;
    }
    
    public List<Object> getByOrderAndCoupon(Integer orderId, Integer couponId) {

        List<OrderCouponsEntity> list = orderCouponsDAO.findByOrderId(orderId)
                .stream()
                .filter(oc -> oc.getCoupon().getCouponId().equals(couponId))
                .toList();

        if (list.isEmpty()) {
            throw new NoRecordsFoundException("No matching record found");
        }

        List<Object> response = new java.util.ArrayList<>();

        for (OrderCouponsEntity oc : list) {

            OrderCoupons orderDTO = new OrderCoupons(
                    oc.getOrder().getOrderId(),
                    oc.getCoupon().getCouponId()
            );

            Coupons couponDTO = new Coupons(
                    oc.getCoupon().getCouponId(),
                    oc.getCoupon().getCouponCode(),
                    oc.getCoupon().getDiscountAmount(),
                    oc.getCoupon().getExpiryDate()
            );

            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("order", orderDTO);
            map.put("coupon", couponDTO);

            response.add(map);
        }

        return response;
    }
    
    public List<Object> getAll() {

        List<OrderCouponsEntity> list = orderCouponsDAO.findAll();

        if (list == null || list.isEmpty()) {
            throw new NoRecordsFoundException("No records found");
        }

        List<Object> response = new java.util.ArrayList<>();

        for (OrderCouponsEntity oc : list) {

            OrderCoupons orderDTO = new OrderCoupons(
                    oc.getOrder().getOrderId(),
                    oc.getCoupon().getCouponId()
            );

            Coupons couponDTO = new Coupons(
                    oc.getCoupon().getCouponId(),
                    oc.getCoupon().getCouponCode(),
                    oc.getCoupon().getDiscountAmount(),
                    oc.getCoupon().getExpiryDate()
            );

            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("order", orderDTO);
            map.put("coupon", couponDTO);

            response.add(map);
        }

        return response;
    }

}