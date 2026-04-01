package com.foodservice.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.exception.ResourceNotFoundException;
import com.foodservice.repository.MenuItemsRepository;

@Repository
public class MenuItemsDAO {

    @Autowired
    private MenuItemsRepository menuItemsRepository;

    // ---------------- BASIC ----------------

    public MenuItemsEntity save(MenuItemsEntity item) {
        return menuItemsRepository.saveAndFlush(item);
    }

    public List<MenuItemsEntity> findAll() {
        return menuItemsRepository.findAll();
    }

    public MenuItemsEntity findById(Integer id) {
        return menuItemsRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Item not found with id: " + id));
    }

    public boolean existsById(Integer id) {
        return menuItemsRepository.existsById(id);
    }

    public void deleteById(Integer id) {
        if (!menuItemsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item not found with id: " + id);
        }
        menuItemsRepository.deleteById(id);
    }

    // ---------------- RELATION ----------------

    public List<MenuItemsEntity> findByRestaurantId(Integer restaurantId) {
        List<MenuItemsEntity> list =
                menuItemsRepository.findByRestaurantRestaurantId(restaurantId);

        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No items found for restaurant id: " + restaurantId);
        }

        return list;
    }

    

    // ---------------- SEARCH ----------------

    public List<MenuItemsEntity> findByName(String name) {
        List<MenuItemsEntity> list =
                menuItemsRepository.findByItemNameContainingIgnoreCase(name);

        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No items found with name: " + name);
        }

        return list;
    }

    public List<MenuItemsEntity> findByPriceRange(Double min, Double max) {
        List<MenuItemsEntity> list =
                menuItemsRepository.findByItemPriceBetween(min, max);

        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No items found in price range");
        }

        return list;
    }

    public List<MenuItemsEntity> findByRestaurantAndPrice(
            Integer restaurantId, Double min, Double max) {

        List<MenuItemsEntity> list =
                menuItemsRepository.findByRestaurantRestaurantIdAndItemPriceBetween(
                        restaurantId, min, max);

        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No items found for given filters");
        }

        return list;
    }

    // ---------------- BULK ----------------

    
    

    // ---------------- SORT ----------------

    public List<MenuItemsEntity> sortItems(String field, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();

        return menuItemsRepository.findAll(sort);
    }

    // ---------------- PAGINATION ----------------

    public Page<MenuItemsEntity> getPagedItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return menuItemsRepository.findAll(pageable);
    }

    // ---------------- COMPLEX METHODS ----------------


    

    // Top selling items (most frequently ordered by quantity)
    public List<MenuItemsEntity> findTopSellingItems() {
        List<MenuItemsEntity> list =
                menuItemsRepository.findTopSellingItems();

        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No top selling items found");
        }

        return list;
    }

    // Items from restaurants that have at least one rating equal to the given value (empty list if none)
    public List<MenuItemsEntity> findItemsByRating(int rating) {
        List<MenuItemsEntity> list = menuItemsRepository.findItemsByRating(rating);
        return list != null ? list : List.of();
    }

    // Items from a specific restaurant with the exact given rating
    public List<Object[]> findItemsByRestaurantAndRating(
            Integer restaurantId, int minRating) {

        List<Object[]> list =
                menuItemsRepository.findItemsByRestaurantAndRating(restaurantId, minRating);

        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No items found for restaurant id: " + restaurantId
                    + " with rating = " + minRating);
        }

        return list;
    }
}