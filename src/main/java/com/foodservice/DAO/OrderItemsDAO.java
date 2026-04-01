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

    

    //  FIXED
    public List<OrderItemsEntity> findByOrderId(Integer orderId) {
        return repository.findByOrder_OrderId(orderId);
    }

    public Integer getMaxId() {
        return repository.findMaxId();
    }
    //---NEW ---
 // GET BY ITEM ID
    public List<OrderItemsEntity> findByItemId(Integer itemId) {
        return repository.findByItem_ItemId(itemId); // already exists in repo!
    }

    // GET BY ORDER ID + ITEM ID
    public List<OrderItemsEntity> findByOrderIdAndItemId(Integer orderId, Integer itemId) {
        return repository.findByOrder_OrderIdAndItem_ItemId(orderId, itemId);
    }

    // GET MOST ORDERED ITEMS
    public List<Object[]> findMostOrderedItems() {
        return repository.findMostOrderedItems();
    }

    

    
}