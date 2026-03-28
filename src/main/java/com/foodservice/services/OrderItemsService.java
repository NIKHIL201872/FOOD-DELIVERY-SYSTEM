package com.foodservice.services;

import com.foodservice.DAO.OrderItemsDAO;
import com.foodservice.dto.OrderItems;
import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.entity.OrderItemsEntity;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.exception.ResourceNotFoundException;
import com.foodservice.repository.MenuItemsRepository;
import com.foodservice.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemsService {

    @Autowired
    private OrderItemsDAO dao;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private MenuItemsRepository menuItemsRepository;

    // ✅ CREATE
    public OrderItems create(OrderItems dto) {

        OrdersEntity order = ordersRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        MenuItemsEntity item = menuItemsRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        Integer newId = dao.getMaxId() + 1;

        OrderItemsEntity entity = new OrderItemsEntity();
        entity.setOrderItemId(newId);
        entity.setQuantity(dto.getQuantity());
        entity.setOrder(order);
        entity.setItem(item);

        return convertToDTO(dao.save(entity));
    }

    // ✅ GET ALL
    public List<OrderItems> getAll() {
        return dao.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ GET BY ID
    public OrderItems getById(Integer id) {
        OrderItemsEntity entity = dao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id " + id));

        return convertToDTO(entity);
    }

    // ✅ UPDATE
    public OrderItems update(Integer id, OrderItems dto) {

        OrderItemsEntity existing = dao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id " + id));

        OrdersEntity order = ordersRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        MenuItemsEntity item = menuItemsRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        existing.setQuantity(dto.getQuantity());
        existing.setOrder(order);
        existing.setItem(item);

        return convertToDTO(dao.save(existing));
    }

    // ✅ DELETE
    public void delete(Integer id) {
        OrderItemsEntity existing = dao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id " + id));

        dao.deleteById(existing.getOrderItemId());
    }

    // ✅ GET BY ORDER ID
    public List<OrderItems> getByOrderId(Integer orderId) {
        return dao.findByOrderId(orderId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ DTO CONVERSION
    private OrderItems convertToDTO(OrderItemsEntity entity) {
        OrderItems dto = new OrderItems();
        dto.setOrderItemId(entity.getOrderItemId());
        dto.setQuantity(entity.getQuantity());
        dto.setOrderId(entity.getOrder().getOrderId());
        dto.setItemId(entity.getItem().getItemId());
        return dto;
    }
}