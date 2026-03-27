package com.foodservice.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.foodservice.DAO.MenuItemsDAO;
import com.foodservice.dto.MenuItems;
import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.exception.BadRequestException;
import com.foodservice.exception.ResourceConflictException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class MenuItemsService {

    @Autowired
    private MenuItemsDAO menuItemsDAO;

    @Autowired
    private ModelMapper mapper;
    
    @PersistenceContext
    private EntityManager entityManager;

    // ---------------- COMMON MAPPER ----------------

    private MenuItems convertToDTO(MenuItemsEntity entity) {
        MenuItems dto = mapper.map(entity, MenuItems.class);
        dto.setRestaurantId(entity.getRestaurant().getRestaurantId());
        return dto;
    }

    // ---------------- CREATE ----------------

    @Transactional
    public MenuItems saveItem(MenuItems dto) {

        if (dto == null) {
            throw new BadRequestException("Item cannot be null");
        }

        if (dto.getItemId() == null) {
            throw new BadRequestException("Item ID must be provided");
        }

        // ✅ CLEAN EXIST CHECK
        if (menuItemsDAO.existsById(dto.getItemId())) {
            throw new ResourceConflictException("Item ID already exists");
        }

        MenuItemsEntity entity = mapper.map(dto, MenuItemsEntity.class);

        RestaurantsEntity restaurant = new RestaurantsEntity();
        restaurant.setRestaurantId(dto.getRestaurantId());
        entity.setRestaurant(restaurant);

        MenuItemsEntity saved = menuItemsDAO.save(entity);

        return convertToDTO(saved);
    }
    // ---------------- READ ----------------

    public List<MenuItems> getAllItems() {

        return menuItemsDAO.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public MenuItems getItemById(Integer id) {

        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid item id");
        }

        return convertToDTO(menuItemsDAO.findById(id));
    }

    // ---------------- UPDATE ----------------

    public MenuItems updateItem(Integer id, MenuItemsEntity item) {

        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid item id");
        }

        if (item == null) {
            throw new BadRequestException("Item data cannot be null");
        }

        MenuItemsEntity existing = menuItemsDAO.findById(id);

        existing.setItemName(item.getItemName());
        existing.setItemDescription(item.getItemDescription());
        existing.setItemPrice(item.getItemPrice());
        existing.setRestaurant(item.getRestaurant());

        return convertToDTO(menuItemsDAO.save(existing));
    }

    // ---------------- DELETE ----------------

    public void deleteItem(Integer id) {

        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid item id");
        }

        menuItemsDAO.deleteById(id);
    }

    // ---------------- RELATION ----------------

    public List<MenuItems> getByRestaurantId(Integer restaurantId) {

        if (restaurantId == null || restaurantId <= 0) {
            throw new BadRequestException("Invalid restaurant id");
        }

        return menuItemsDAO.findByRestaurantId(restaurantId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItems> getItemsByOrderId(Integer orderId) {

        if (orderId == null || orderId <= 0) {
            throw new BadRequestException("Invalid order id");
        }

        return menuItemsDAO.findItemsByOrderId(orderId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ---------------- SEARCH ----------------

    public List<MenuItems> getByName(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Name cannot be empty");
        }

        return menuItemsDAO.findByName(name)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItems> getByPriceRange(Double min, Double max) {

        if (min == null || max == null || min < 0 || max < 0 || min > max) {
            throw new BadRequestException("Invalid price range");
        }

        return menuItemsDAO.findByPriceRange(min, max)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItems> getByRestaurantAndPrice(Integer restaurantId, Double min, Double max) {

        if (restaurantId == null || restaurantId <= 0) {
            throw new BadRequestException("Invalid restaurant id");
        }

        if (min == null || max == null || min < 0 || max < 0 || min > max) {
            throw new BadRequestException("Invalid price range");
        }

        return menuItemsDAO.findByRestaurantAndPrice(restaurantId, min, max)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ---------------- BULK ----------------

    public List<MenuItems> saveAll(List<MenuItemsEntity> items) {

        if (items == null || items.isEmpty()) {
            throw new BadRequestException("Item list cannot be empty");
        }

        return menuItemsDAO.saveAll(items)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public void deleteByRestaurant(Integer restaurantId) {

        if (restaurantId == null || restaurantId <= 0) {
            throw new BadRequestException("Invalid restaurant id");
        }

        menuItemsDAO.deleteByRestaurant(restaurantId);
    }

    // ---------------- SORT ----------------

    public List<MenuItems> sortItems(String field, String direction) {

        if (field == null || field.isEmpty()) {
            throw new BadRequestException("Sort field cannot be empty");
        }

        if (!direction.equalsIgnoreCase("asc") &&
            !direction.equalsIgnoreCase("desc")) {
            throw new BadRequestException("Direction must be 'asc' or 'desc'");
        }

        return menuItemsDAO.sortItems(field, direction)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ---------------- PAGINATION ----------------

    public Page<MenuItemsEntity> getPagedItems(int page, int size) {

        if (page < 0 || size <= 0) {
            throw new BadRequestException("Invalid page or size");
        }

        return menuItemsDAO.getPagedItems(page, size);
    }

    // ---------------- COMPLEX APIs ----------------

    public List<MenuItems> getItemsByCustomer(Integer customerId) {

        if (customerId == null || customerId <= 0) {
            throw new BadRequestException("Invalid customer id");
        }

        return menuItemsDAO.findItemsByCustomer(customerId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItems> getItemsByOrderDate(String date) {

        if (date == null || date.isEmpty()) {
            throw new BadRequestException("Date cannot be empty");
        }

        return menuItemsDAO.findItemsByOrderDate(date)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItems> getTopSellingItems() {

        return menuItemsDAO.findTopSellingItems()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItems> getItemsByRating(int rating) {

        if (rating < 0 || rating > 5) {
            throw new BadRequestException("Rating must be between 0 and 5");
        }

        return menuItemsDAO.findItemsByRating(rating)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
}