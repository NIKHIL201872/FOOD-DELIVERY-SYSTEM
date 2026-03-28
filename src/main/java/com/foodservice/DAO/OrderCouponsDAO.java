package com.foodservice.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.foodservice.entity.*;
import com.foodservice.repository.OrderCouponsRepository;
//import com.foodservice.repository.OrdersRepository;

@Repository
public class OrderCouponsDAO {

    @Autowired
    private OrderCouponsRepository repo;

    /*
     * @Autowired
     * private OrdersRepository orderRepo;
     */
    public OrderCouponsEntity save(OrderCouponsEntity oc) {
        return repo.save(oc);
    }

    public List<OrderCouponsEntity> findAll() {
        return repo.findAll();
    }

    public List<OrderCouponsEntity> findByOrderId(Integer orderId) {
        return repo.findByOrder_OrderId(orderId);
    }

    public List<OrderCouponsEntity> findByCouponId(Integer couponId) {
        return repo.findByCoupon_CouponId(couponId);
    }

    public void delete(OrderCouponsEntity oc) {
        repo.delete(oc);
    }

    public void deleteByCouponId(Integer couponId) {
        repo.deleteByCoupon_CouponId(couponId);
    }

    /*
     * public OrdersEntity findOrderById(Integer orderId) {
     * return orderRepo.findById(orderId).orElse(null);
     * }
     */
}