package com.foodservice.DAO;

import com.foodservice.entity.OrderItemsEntity;
import com.foodservice.repository.OrderItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderItemsDAO {

    @Autowired
    private OrderItemsRepository repository;

    public OrderItemsEntity save(OrderItemsEntity entity) {
        return repository.save(entity);
    }

    public List<OrderItemsEntity> findAll() {
        return repository.findAll();
    }

    public Optional<OrderItemsEntity> findById(Integer id) {
        return repository.findById(id);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    // ✅ FIXED
    public List<OrderItemsEntity> findByOrderId(Integer orderId) {
        return repository.findByOrder_OrderId(orderId);
    }

    public Integer getMaxId() {
        return repository.findMaxId();
    }
}