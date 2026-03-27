package com.foodservice.DAO;

import com.foodservice.entity.OrdersEntity;
import com.foodservice.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrdersDAO {

    private final OrdersRepository orderRepo;

    public OrdersEntity save(OrdersEntity order) {
        return orderRepo.save(order);
    }

    public OrdersEntity findById(Integer id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<OrdersEntity> findAll() {
        return orderRepo.findAll();
    }
}