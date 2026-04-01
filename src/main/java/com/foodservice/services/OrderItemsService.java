package com.foodservice.services;

import com.foodservice.DAO.OrderItemsDAO;
import com.foodservice.dto.OrderItems;
import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.entity.OrderItemsEntity;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.exception.OrderItemConflictException;
import com.foodservice.exception.OrderItemNotFoundException;
import com.foodservice.exception.ResourceNotFoundException;
import com.foodservice.repository.MenuItemsRepository;
import com.foodservice.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    private void validateOrderAndItemIds(OrderItems dto) {
        if (dto == null) {
            throw new OrderItemNotFoundException("Order item payload not found");
        }
        if (dto.getOrderId() == null) {
            throw new OrderItemNotFoundException("Order id not found in request");
        }
        if (dto.getItemId() == null) {
            throw new OrderItemNotFoundException("Item id not found in request");
        }
    }

    // ─────────────────────────────────────────
    //  CREATE
    // ─────────────────────────────────────────
    public String create(OrderItems dto) {
        validateOrderAndItemIds(dto);
        OrdersEntity order = ordersRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new OrderItemNotFoundException("Order not found"));
        MenuItemsEntity item = menuItemsRepository.findById(dto.getItemId())
                .orElseThrow(() -> new OrderItemNotFoundException("Item not found"));

        Integer maxId = dao.getMaxId();
        Integer newId = (maxId == null ? 1 : maxId + 1);
        dao.findById(newId).ifPresent(existing -> {
            throw new OrderItemConflictException("OrderItem with id " + newId + " already exists");
        });
        OrderItemsEntity entity = new OrderItemsEntity();
        entity.setOrderItemId(newId);
        entity.setQuantity(dto.getQuantity());
        entity.setOrder(order);
        entity.setItem(item);

        dao.save(entity);
        return "Order item inserted successfully with ID: " + entity.getOrderItemId();
    }

    
    // ─────────────────────────────────────────
    //  GET ALL
    // ─────────────────────────────────────────
    public List<OrderItems> getAll() {
        return dao.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────
    //  GET BY ID
    // ─────────────────────────────────────────
    public OrderItems getById(Integer id) {
        if (id == null) {
            throw new OrderItemNotFoundException("OrderItem id not found in request");
        }
        OrderItemsEntity entity = dao.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException(
                        "OrderItem not found with id " + id));
        return convertToDTO(entity);
    }

    // ─────────────────────────────────────────
    //  UPDATE BY ID
    // ─────────────────────────────────────────
    public String update(Integer id, OrderItems dto) {
        if (id == null) {
            throw new OrderItemNotFoundException("OrderItem id not found in request");
        }
        validateOrderAndItemIds(dto);
        OrderItemsEntity existing = dao.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException(
                        "OrderItem not found with id " + id));
        OrdersEntity order = ordersRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new OrderItemNotFoundException("Order not found"));
        MenuItemsEntity item = menuItemsRepository.findById(dto.getItemId())
                .orElseThrow(() -> new OrderItemNotFoundException("Item not found"));

        existing.setQuantity(dto.getQuantity());
        existing.setOrder(order);
        existing.setItem(item);

        dao.save(existing);
        return "Order item updated successfully for ID: " + id;
    }

    

    // ─────────────────────────────────────────
    //  GET BY ORDER ID
    // ─────────────────────────────────────────
    public List<OrderItems> getByOrderId(Integer orderId) {
        if (orderId == null) {
            throw new OrderItemNotFoundException("Order id not found in request");
        }
        ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderItemNotFoundException(
                        "Order not found with id " + orderId));
        return dao.findByOrderId(orderId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────
    //  1. GET BY ITEM ID
    // ─────────────────────────────────────────
    public List<OrderItems> getByItemId(Integer itemId) {
        if (itemId == null) {
            throw new OrderItemNotFoundException("Item id not found in request");
        }
        menuItemsRepository.findById(itemId)
                .orElseThrow(() -> new OrderItemNotFoundException(
                        "Item not found with id " + itemId));
        return dao.findByItemId(itemId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────
    //  2. GET BY ORDER ID + ITEM ID
    // ─────────────────────────────────────────
    public List<OrderItems> getByOrderAndItem(Integer orderId, Integer itemId) {
        if (orderId == null) {
            throw new OrderItemNotFoundException("Order id not found in request");
        }
        if (itemId == null) {
            throw new OrderItemNotFoundException("Item id not found in request");
        }
        List<OrderItemsEntity> entities = dao.findByOrderIdAndItemId(orderId, itemId);
        if (entities.isEmpty()) {
            throw new OrderItemNotFoundException(
                    "No order item found for orderId " + orderId + " and itemId " + itemId);
        }
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────
    //  3. GET TOTAL QUANTITY BY ORDER ID
    // ─────────────────────────────────────────
    public Integer getTotalQuantityByOrder(Integer orderId) {
        if (orderId == null) {
            throw new OrderItemNotFoundException("Order id not found in request");
        }
        ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderItemNotFoundException(
                        "Order not found with id " + orderId));
        List<OrderItemsEntity> items = dao.findByOrderId(orderId);
        if (items.isEmpty()) {
            throw new OrderItemNotFoundException("No items found for orderId " + orderId);
        }
        return items.stream()
                .mapToInt(OrderItemsEntity::getQuantity)
                .sum();
    }

    // ─────────────────────────────────────────
    //  4. GET TOTAL PRICE BY ORDER ID
    // ─────────────────────────────────────────
    public Double getTotalPriceByOrder(Integer orderId) {
        if (orderId == null) {
            throw new OrderItemNotFoundException("Order id not found in request");
        }
        ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderItemNotFoundException(
                        "Order not found with id " + orderId));
        List<OrderItemsEntity> items = dao.findByOrderId(orderId);
        if (items.isEmpty()) {
            throw new OrderItemNotFoundException("No items found for orderId " + orderId);
        }
        return items.stream()
                .mapToDouble(entity ->
                        entity.getItem().getItemPrice().doubleValue() * entity.getQuantity())
                .sum();
    }

    // ─────────────────────────────────────────
    //  5. GET MOST ORDERED ITEMS
    // ─────────────────────────────────────────
    public List<OrderItems> getMostOrderedItems() {
        List<Object[]> result = dao.findMostOrderedItems();
        if (result.isEmpty()) {
            throw new OrderItemNotFoundException("No order items data found");
        }
        return result.stream().map(row -> {
            OrderItems dto = new OrderItems();
            dto.setItemId((Integer) row[0]);
            dto.setItemName((String) row[1]);
            dto.setQuantity(((Long) row[2]).intValue());
            dto.setItemPrice((BigDecimal) row[3]);
            return dto;
        }).collect(Collectors.toList());
    }

    
    

    // ─────────────────────────────────────────
    //  6. UPDATE QUANTITY ONLY (PATCH)
    // ─────────────────────────────────────────
    public String updateQuantity(Integer id, Integer quantity) {
        if (id == null) {
            throw new OrderItemNotFoundException("OrderItem id not found in request");
        }
        if (quantity <= 0) {
            throw new OrderItemNotFoundException("Quantity parameter not found or invalid");
        }
        OrderItemsEntity existing = dao.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException(
                        "OrderItem not found with id " + id));
        existing.setQuantity(quantity);
        dao.save(existing);
        return "Order item quantity updated successfully for ID: " + id;
    }

    // ─────────────────────────────────────────
    //  7. GET ENRICHED ORDER ITEM DETAILS
    // ─────────────────────────────────────────
    public List<OrderItems> getOrderItemDetails(Integer orderId) {
        if (orderId == null) {
            throw new OrderItemNotFoundException("Order id not found in request");
        }
        ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderItemNotFoundException(
                        "Order not found with id " + orderId));
        List<OrderItemsEntity> items = dao.findByOrderId(orderId);
        if (items.isEmpty()) {
            throw new OrderItemNotFoundException("No items found for orderId " + orderId);
        }
        return items.stream()
                .map(entity -> {
                    OrderItems dto = new OrderItems();
                    dto.setOrderItemId(entity.getOrderItemId());
                    dto.setOrderId(entity.getOrder().getOrderId());
                    dto.setItemId(entity.getItem().getItemId());
                    dto.setQuantity(entity.getQuantity());
                    dto.setItemName(entity.getItem().getItemName());
                    dto.setItemPrice(entity.getItem().getItemPrice());
                    dto.setSubtotal(entity.getItem().getItemPrice()
                            .multiply(BigDecimal.valueOf(entity.getQuantity())));
                    return dto;
                })
                .collect(Collectors.toList());
    }
		 // ─────────────────────────────────────────
		//  8. GET MENU ITEM BY ORDER ITEM ID
		// ─────────────────────────────────────────
		public OrderItems getMenuItemByOrderItemId(Integer id) {
		    if (id == null) {
		        throw new OrderItemNotFoundException("OrderItem id not found in request");
		    }
		    OrderItemsEntity entity = dao.findById(id)
		            .orElseThrow(() -> new OrderItemNotFoundException(
		                    "OrderItem not found with id " + id));
		
		    OrderItems dto = new OrderItems();
		    dto.setOrderItemId(entity.getOrderItemId());
		    dto.setOrderId(entity.getOrder().getOrderId());
		    dto.setItemId(entity.getItem().getItemId());
		    dto.setItemName(entity.getItem().getItemName());
		    dto.setItemPrice(entity.getItem().getItemPrice());
		    dto.setQuantity(entity.getQuantity());
		    dto.setSubtotal(entity.getItem().getItemPrice()
		            .multiply(BigDecimal.valueOf(entity.getQuantity())));
		    dto.setItemDescription(entity.getItem().getItemDescription());
		    dto.setRestaurantid(entity.getItem().getRestaurant().getRestaurantId());
		    return dto;
		}

    // ─────────────────────────────────────────
    //  COMMON DTO CONVERSION
    // ─────────────────────────────────────────
    private OrderItems convertToDTO(OrderItemsEntity entity) {
        OrderItems dto = new OrderItems();
        dto.setOrderItemId(entity.getOrderItemId());
        dto.setQuantity(entity.getQuantity());
        dto.setOrderId(entity.getOrder().getOrderId());
        dto.setItemId(entity.getItem().getItemId());
        return dto;
    }
}