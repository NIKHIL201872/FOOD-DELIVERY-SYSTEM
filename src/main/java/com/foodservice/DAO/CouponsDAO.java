package com.foodservice.DAO;

import com.foodservice.entity.*;
import com.foodservice.repository.*;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CouponsDAO {

    @Autowired
    private CouponsRepository repo;

    @Autowired
    private OrderCouponsRepository ocRepo;

    public CouponsEntity save(CouponsEntity c) {
        return repo.save(c);
    }

    public List<CouponsEntity> findAll() {
        return repo.findAll();
    }

    public CouponsEntity findById(Integer id) {
        return repo.findById(id).orElseThrow();
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    public void saveOrderCoupon(OrderCouponsEntity oc) {
        ocRepo.save(oc);
    }

    public List<OrderCouponsEntity> findCouponsByOrder(Integer orderId) {
        return ocRepo.findByOrder_OrderId(orderId);
    }

    public List<CouponsEntity> searchDynamic(CouponsEntity filter) {

        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreNullValues().withMatcher("couponCode",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<CouponsEntity> example = Example.of(filter, matcher);

        return repo.findAll(example);
    }
}