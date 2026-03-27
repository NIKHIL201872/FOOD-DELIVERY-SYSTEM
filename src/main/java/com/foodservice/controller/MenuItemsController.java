package com.foodservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodservice.dto.MenuItems;
import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.services.MenuItemsService;

@RestController
@RequestMapping("/menuitems")
public class MenuItemsController {

    @Autowired
    private MenuItemsService menuItemsService;

    // -------------------- BASIC CRUD --------------------

    @PostMapping
    public ResponseEntity<MenuItemsEntity> createItem(@RequestBody MenuItemsEntity item) {
        return new ResponseEntity<>(menuItemsService.saveItem(item), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MenuItems>> getAllItems() {
        return ResponseEntity.ok(menuItemsService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItems> getItemById(@PathVariable ("id")Integer id) {
        return new ResponseEntity<MenuItems>(menuItemsService.getItemById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemsEntity> updateItem(
            @PathVariable Integer id,
            @RequestBody MenuItemsEntity item) {
        return ResponseEntity.ok(menuItemsService.updateItem(id, item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Integer id) {
        menuItemsService.deleteItem(id);
        return ResponseEntity.ok("Item deleted successfully");
    }

    // -------------------- RELATIONSHIP APIs --------------------

    // Get all items by restaurant
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemsEntity>> getByRestaurant(@PathVariable Integer restaurantId) {
        return ResponseEntity.ok(menuItemsService.getByRestaurantId(restaurantId));
    }

    // Get items used in orders (via OrderItems)
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<MenuItemsEntity>> getItemsByOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(menuItemsService.getItemsByOrderId(orderId));
    }

    // -------------------- SEARCH / FILTER APIs --------------------

    @GetMapping("/search")
    public ResponseEntity<List<MenuItemsEntity>> searchItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer restaurantId) {

        return ResponseEntity.ok(
                menuItemsService.searchItems(name, minPrice, maxPrice, restaurantId)
        );
    }

    // -------------------- PARTIAL UPDATE (PATCH) --------------------

    @PatchMapping("/{id}")
    public ResponseEntity<MenuItemsEntity> partialUpdate(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {

        return ResponseEntity.ok(menuItemsService.partialUpdate(id, updates));
    }

    // -------------------- BULK OPERATIONS --------------------

    @PostMapping("/bulk")
    public ResponseEntity<List<MenuItemsEntity>> saveAll(@RequestBody List<MenuItemsEntity> items) {
        return new ResponseEntity<>(menuItemsService.saveAll(items), HttpStatus.CREATED);
    }

    @DeleteMapping("/restaurant/{restaurantId}")
    public ResponseEntity<String> deleteByRestaurant(@PathVariable Integer restaurantId) {
        menuItemsService.deleteByRestaurant(restaurantId);
        return ResponseEntity.ok("Deleted all items of restaurant");
    }

    // -------------------- SORTING --------------------

    @GetMapping("/sort")
    public ResponseEntity<List<MenuItemsEntity>> sortItems(
            @RequestParam(defaultValue = "itemPrice") String field,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(menuItemsService.sortItems(field, direction));
    }

    // -------------------- PAGINATION --------------------

    @GetMapping("/page")
    public ResponseEntity<Page<MenuItemsEntity>> getPagedItems(
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(menuItemsService.getPagedItems(page, size));
    }

    // -------------------- ADVANCED DYNAMIC API --------------------

    @PostMapping("/dynamic")
    public ResponseEntity<List<MenuItemsEntity>> dynamicFilter(
            @RequestBody Map<String, Object> filters) {

        return ResponseEntity.ok(menuItemsService.dynamicFilter(filters));
    }
}