package com.foodservice.services;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.foodservice.DAO.MenuItemsDAO;
import com.foodservice.dto.MenuItemWithRating;
import com.foodservice.dto.MenuItems;
import com.foodservice.dto.PagedResponse;
import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.exception.MenuItemConflictException;
import com.foodservice.exception.MenuItemNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class MenuItemsService {

    @Autowired
    private MenuItemsDAO menuItemsDAO;

    @Autowired
    private ModelMapper mapper;

    // ---------------- COMMON MAPPER ----------------

    private MenuItems convertToDTO(MenuItemsEntity entity) {
        MenuItems dto = mapper.map(entity, MenuItems.class);
        if (entity.getRestaurant() == null || entity.getRestaurant().getRestaurantId() == null) {
            throw new MenuItemNotFoundException("Restaurant not found for menu item id: " + entity.getItemId());
        }
        dto.setRestaurantId(entity.getRestaurant().getRestaurantId());
        return dto;
    }

    private MenuItemsEntity convertToEntity(MenuItems dto) {
        MenuItemsEntity entity = mapper.map(dto, MenuItemsEntity.class);
        RestaurantsEntity restaurant = new RestaurantsEntity();
        restaurant.setRestaurantId(dto.getRestaurantId());
        entity.setRestaurant(restaurant);
        return entity;
    }

    // ---------------- CREATE ----------------

    @Transactional
    public String saveItem(MenuItems dto) {
        if (dto == null) {
            throw new MenuItemNotFoundException("Item payload not found");
        }
        if (dto.getItemId() == null) {
            throw new MenuItemNotFoundException("Item ID not found in request");
        }
        if (menuItemsDAO.existsById(dto.getItemId())) {
            throw new MenuItemConflictException("Item ID already exists");
        }
        menuItemsDAO.save(convertToEntity(dto));
        return "Menu item inserted successfully with ID: " + dto.getItemId();
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
            throw new MenuItemNotFoundException("Item id not found");
        }
        return convertToDTO(menuItemsDAO.findById(id));
    }

    // ---------------- UPDATE ----------------

    @Transactional
    public String updateItem(Integer id, MenuItems dto) {
        if (id == null || id <= 0) {
            throw new MenuItemNotFoundException("Item id not found");
        }
        if (dto == null) {
            throw new MenuItemNotFoundException("Item payload not found");
        }

        MenuItemsEntity existing = menuItemsDAO.findById(id);
        existing.setItemName(dto.getItemName());
        existing.setItemDescription(dto.getItemDescription());
        existing.setItemPrice(BigDecimal.valueOf(dto.getItemPrice()));

        if (dto.getRestaurantId() != null && dto.getRestaurantId() > 0) {
            RestaurantsEntity restaurant = new RestaurantsEntity();
            restaurant.setRestaurantId(dto.getRestaurantId());
            existing.setRestaurant(restaurant);
        }

        menuItemsDAO.save(existing);
        return "Menu item updated successfully for ID: " + id;
    }

    @Transactional
    public String updatePrice(Integer id, Double price) {
        if (id == null || id <= 0) {
            throw new MenuItemNotFoundException("Item id not found");
        }
        if (price == null || price < 0) {
            throw new MenuItemNotFoundException("Price parameter not found or invalid");
        }

        MenuItemsEntity existing = menuItemsDAO.findById(id);
        existing.setItemPrice(BigDecimal.valueOf(price));
        menuItemsDAO.save(existing);
        return "Menu item price updated successfully for ID: " + id;
    }

    // ---------------- DELETE ----------------

    public String deleteItem(Integer id) {
        if (id == null || id <= 0) {
            throw new MenuItemNotFoundException("Item id not found");
        }
        menuItemsDAO.deleteById(id);
        return "Menu item deleted successfully for ID: " + id;
    }

    public String deleteByRestaurant(Integer restaurantId) {
        if (restaurantId == null || restaurantId <= 0) {
            throw new MenuItemNotFoundException("Restaurant id not found");
        }
        menuItemsDAO.deleteByRestaurant(restaurantId);
        return "All menu items deleted successfully for Restaurant ID: " + restaurantId;
    }

    // ---------------- RELATION ----------------

    public List<MenuItems> getByRestaurantId(Integer restaurantId) {
        if (restaurantId == null || restaurantId <= 0) {
            throw new MenuItemNotFoundException("Restaurant id not found");
        }
        return menuItemsDAO.findByRestaurantId(restaurantId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItems> getItemsByOrderId(Integer orderId) {
        if (orderId == null || orderId <= 0) {
            throw new MenuItemNotFoundException("Order id not found");
        }
        return menuItemsDAO.findItemsByOrderId(orderId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ---------------- SEARCH ----------------

    public List<MenuItems> getByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new MenuItemNotFoundException("Name parameter not found");
        }
        return menuItemsDAO.findByName(name)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItems> getByPriceRange(Double min, Double max) {
        if (min == null || max == null || min < 0 || max < 0 || min > max) {
            throw new MenuItemNotFoundException("Price range parameters not found or invalid");
        }
        return menuItemsDAO.findByPriceRange(min, max)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItems> getByRestaurantAndPrice(Integer restaurantId, Double min, Double max) {
        if (restaurantId == null || restaurantId <= 0) {
            throw new MenuItemNotFoundException("Restaurant id not found");
        }
        if (min == null || max == null || min < 0 || max < 0 || min > max) {
            throw new MenuItemNotFoundException("Price range parameters not found or invalid");
        }
        return menuItemsDAO.findByRestaurantAndPrice(restaurantId, min, max)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ---------------- BULK ----------------

    @Transactional
    public String saveAll(List<MenuItems> items) {
        if (items == null || items.isEmpty()) {
            throw new MenuItemNotFoundException("Item list payload not found");
        }
        List<MenuItemsEntity> entities = items.stream()
                .map(this::convertToEntity)
                .toList();
        menuItemsDAO.saveAll(entities);
        return items.size() + " menu item(s) inserted successfully";
    }

    // ---------------- SORT ----------------

    public List<MenuItems> sortItems(String field, String direction) {
        if (field == null || field.isEmpty()) {
            throw new MenuItemNotFoundException("Sort field parameter not found");
        }
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new MenuItemNotFoundException("Sort direction parameter not found or invalid");
        }
        return menuItemsDAO.sortItems(field, direction)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ---------------- PAGINATION ----------------

    public PagedResponse<MenuItems> getPagedItems(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new MenuItemNotFoundException("Page or size parameters not found or invalid");
        }
        Page<MenuItemsEntity> result = menuItemsDAO.getPagedItems(page, size);
        List<MenuItems> items = result.getContent()
                .stream()
                .map(this::convertToDTO)
                .toList();
        return new PagedResponse<>(
                items,
                result.getNumber(),
                result.getTotalPages(),
                result.getTotalElements(),
                result.getNumberOfElements()
        );
    }

    // ---------------- COMPLEX APIs ----------------

    public List<MenuItems> getItemsByCustomer(Integer customerId) {
        if (customerId == null || customerId <= 0) {
            throw new MenuItemNotFoundException("Customer id not found");
        }
        return menuItemsDAO.findItemsByCustomer(customerId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItems> getItemsByOrderDate(String date) {
        if (date == null || date.isEmpty()) {
            throw new MenuItemNotFoundException("Date parameter not found");
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

    public List<MenuItemWithRating> getItemsByRating(int rating) {
        if (rating < 0 || rating > 5) {
            throw new MenuItemNotFoundException("Rating parameter not found or invalid");
        }
        return menuItemsDAO.findItemsByRating(rating)
                .stream()
                .map(entity -> toMenuItemWithRating(entity, rating))
                .toList();
    }

    private MenuItemWithRating toMenuItemWithRating(MenuItemsEntity entity, int matchedRating) {
        MenuItems base = convertToDTO(entity);
        return new MenuItemWithRating(
                base.getItemId(),
                base.getItemName(),
                base.getItemDescription(),
                base.getItemPrice(),
                base.getRestaurantId(),
                matchedRating);
    }

    public List<MenuItemWithRating> getItemsByRestaurantAndRating(Integer restaurantId, int minRating) {
        if (restaurantId == null || restaurantId <= 0) {
            throw new MenuItemNotFoundException("Restaurant id not found");
        }
        if (minRating < 0 || minRating > 5) {
            throw new MenuItemNotFoundException("Rating parameter not found or invalid");
        }
        return menuItemsDAO.findItemsByRestaurantAndRating(restaurantId, minRating)
                .stream()
                .map(row -> {
                    MenuItemsEntity entity = (MenuItemsEntity) row[0];
                    int actualRating = ((Number) row[1]).intValue();
                    return toMenuItemWithRating(entity, actualRating);
                })
                .toList();
    }
}