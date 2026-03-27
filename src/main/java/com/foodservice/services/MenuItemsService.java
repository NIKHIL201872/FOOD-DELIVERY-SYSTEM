package com.foodservice.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.foodservice.DAO.MenuItemsDAO;
import com.foodservice.dto.MenuItems;
import com.foodservice.entity.MenuItemsEntity;

@Service
public class MenuItemsService {

    @Autowired
    private MenuItemsDAO menuItemsDAO;
    
    @Autowired
    private ModelMapper mapper;

    public MenuItemsEntity saveItem(MenuItemsEntity item) {
        return menuItemsDAO.save(item);
    }

    public List<MenuItems> getAllItems() {
        List<MenuItemsEntity> li = menuItemsDAO.findAll();

        if (li != null) {
            return li.stream().map(entity -> {
                MenuItems m = mapper.map(entity, MenuItems.class);
                m.setRestaurantId(entity.getRestaurant().getRestaurantId());
                return m;
            }).toList();
        } else {
            throw new RuntimeException("No customers available");
        }
    }

    public MenuItems getItemById(Integer id) {
    	
    	 MenuItemsEntity itemsEntity = menuItemsDAO.findById(id);
    	 
    	 MenuItems m = mapper.map(itemsEntity, MenuItems.class);
    	 
    	 m.setRestaurantId(itemsEntity.getRestaurant().getRestaurantId());
    	 //System.out.println(m);
        return m;
    }

    public MenuItemsEntity updateItem(Integer id, MenuItemsEntity item) {
        MenuItemsEntity existing = menuItemsDAO.findById(id);

        existing.setItemName(item.getItemName());
        existing.setItemDescription(item.getItemDescription());
        existing.setItemPrice(item.getItemPrice());
        existing.setRestaurant(item.getRestaurant());

        return menuItemsDAO.save(existing);
    }

    public void deleteItem(Integer id) {
        menuItemsDAO.deleteById(id);
    }

    // ---------------- RELATION ----------------

    public List<MenuItemsEntity> getByRestaurantId(Integer restaurantId) {
        return menuItemsDAO.findByRestaurantId(restaurantId);
    }

    public List<MenuItemsEntity> getItemsByOrderId(Integer orderId) {
        return menuItemsDAO.findItemsByOrderId(orderId);
    }

    // ---------------- SEARCH ----------------

    public List<MenuItemsEntity> searchItems(String name, Double minPrice,
                                             Double maxPrice, Integer restaurantId) {
        return menuItemsDAO.searchItems(name, minPrice, maxPrice, restaurantId);
    }

    // ---------------- PARTIAL UPDATE ----------------

    public MenuItemsEntity partialUpdate(Integer id, Map<String, Object> updates) {
        MenuItemsEntity item = menuItemsDAO.findById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "itemName":
                    item.setItemName((String) value);
                    break;
                case "itemDescription":
                    item.setItemDescription((String) value);
                    break;
                case "itemPrice":
                    item.setItemPrice(Double.valueOf(value.toString()));
                    break;
            }
        });

        return menuItemsDAO.save(item);
    }

    // ---------------- BULK ----------------

    public List<MenuItemsEntity> saveAll(List<MenuItemsEntity> items) {
        return menuItemsDAO.saveAll(items);
    }

    public void deleteByRestaurant(Integer restaurantId) {
        menuItemsDAO.deleteByRestaurant(restaurantId);
    }

    // ---------------- SORT ----------------

    public List<MenuItemsEntity> sortItems(String field, String direction) {
        return menuItemsDAO.sortItems(field, direction);
    }

    // ---------------- PAGINATION ----------------

    public Page<MenuItemsEntity> getPagedItems(int page, int size) {
        return menuItemsDAO.getPagedItems(page, size);
    }

    // ---------------- DYNAMIC ----------------

    public List<MenuItemsEntity> dynamicFilter(Map<String, Object> filters) {
        return menuItemsDAO.dynamicFilter(filters);
    }
}