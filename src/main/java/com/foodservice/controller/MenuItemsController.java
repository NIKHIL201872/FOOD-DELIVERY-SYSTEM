package com.foodservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodservice.dto.MenuItemWithRating;
import com.foodservice.dto.MenuItems;
import com.foodservice.dto.PagedResponse;
import com.foodservice.services.MenuItemsService;

@RestController
@RequestMapping("/menuitems")
public class MenuItemsController {

    @Autowired
    private MenuItemsService menuItemsService;

    // ---------------- CREATE ----------------

    // 1.
    @PostMapping
    public ResponseEntity<String> createItem(@RequestBody MenuItems item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemsService.saveItem(item));
    }

    
    // ---------------- READ ----------------

    // 2.
    @GetMapping
    public ResponseEntity<List<MenuItems>> getAllItems() {
        return ResponseEntity.ok(menuItemsService.getAllItems());
    }

    // 3.
    @GetMapping("/{id}")
    public ResponseEntity<MenuItems> getItemById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(menuItemsService.getItemById(id));
    }

    // ---------------- UPDATE ----------------

    // 4. Full update
    @PutMapping("/{id}")
    public ResponseEntity<String> updateItem(
            @PathVariable("id") Integer id,
            @RequestBody MenuItems item) {
        return ResponseEntity.ok(menuItemsService.updateItem(id, item));
    }

    

    // ---------------- DELETE ----------------

    // 5.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(menuItemsService.deleteItem(id));
    }

    

    // ---------------- RELATION ----------------

    // 6.
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItems>> getByRestaurant(
            @PathVariable("restaurantId") Integer restaurantId) {
        return ResponseEntity.ok(menuItemsService.getByRestaurantId(restaurantId));
    }


    // ---------------- SEARCH ----------------

    // 7.
    @GetMapping("/name/{name}")
    public ResponseEntity<List<MenuItems>> getByName(
            @PathVariable("name") String name) {
        return ResponseEntity.ok(menuItemsService.getByName(name));
    }

    // ---------------- PRICE FILTERS ----------------

    // 8.
    @GetMapping("/price-range")
    public ResponseEntity<List<MenuItems>> getByPriceRange(
            @RequestParam("min") Double min,
            @RequestParam("max") Double max) {
        return ResponseEntity.ok(menuItemsService.getByPriceRange(min, max));
    }

    // 9.
    @GetMapping("/restaurant/{restaurantId}/price-range")
    public ResponseEntity<List<MenuItems>> getByRestaurantAndPrice(
            @PathVariable("restaurantId") Integer restaurantId,
            @RequestParam("min") Double min,
            @RequestParam("max") Double max) {
        return ResponseEntity.ok(
                menuItemsService.getByRestaurantAndPrice(restaurantId, min, max));
    }

    // ---------------- COMPLEX APIs ----------------

    
    
    // 10.
    @GetMapping("/top-selling")
    public ResponseEntity<List<MenuItems>> getTopSellingItems() {
        return ResponseEntity.ok(menuItemsService.getTopSellingItems());
    }

    // 11.
    @GetMapping("/rating")
    public ResponseEntity<List<MenuItemWithRating>> getItemsByRating(
            @RequestParam("rating") int rating) {
        return ResponseEntity.ok(menuItemsService.getItemsByRating(rating));
    }

    // 12.
    @GetMapping("/restaurant/{restaurantId}/rating")
    public ResponseEntity<List<MenuItemWithRating>> getItemsByRestaurantAndRating(
            @PathVariable("restaurantId") Integer restaurantId,
            @RequestParam("min") int minRating) {
        return ResponseEntity.ok(
                menuItemsService.getItemsByRestaurantAndRating(restaurantId, minRating));
    }

    // ---------------- SORT ----------------

    // 13.
    @GetMapping("/sort")
    public ResponseEntity<List<MenuItems>> sortItems(
            @RequestParam(value = "field", defaultValue = "itemPrice") String field,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        return ResponseEntity.ok(menuItemsService.sortItems(field, direction));
    }

    // ---------------- PAGINATION ----------------

    // 14.
    @GetMapping("/page")
    public ResponseEntity<PagedResponse<MenuItems>> getPagedItems(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        return ResponseEntity.ok(menuItemsService.getPagedItems(page, size));
    }
}