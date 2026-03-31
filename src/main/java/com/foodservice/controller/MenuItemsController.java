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

    // 2.
    @PostMapping("/bulk")
    public ResponseEntity<String> saveAll(@RequestBody List<MenuItems> items) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemsService.saveAll(items));
    }

    // ---------------- READ ----------------

    // 3.
    @GetMapping
    public ResponseEntity<List<MenuItems>> getAllItems() {
        return ResponseEntity.ok(menuItemsService.getAllItems());
    }

    // 4.
    @GetMapping("/{id}")
    public ResponseEntity<MenuItems> getItemById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(menuItemsService.getItemById(id));
    }

    // ---------------- UPDATE ----------------

    // 5. Full update
    @PutMapping("/{id}")
    public ResponseEntity<String> updateItem(
            @PathVariable("id") Integer id,
            @RequestBody MenuItems item) {
        return ResponseEntity.ok(menuItemsService.updateItem(id, item));
    }

    // 6. Partial update — price only
    @PatchMapping("/{id}/price")
    public ResponseEntity<String> updatePrice(
            @PathVariable("id") Integer id,
            @RequestParam("price") Double price) {
        return ResponseEntity.ok(menuItemsService.updatePrice(id, price));
    }

    // ---------------- DELETE ----------------

    // 7.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(menuItemsService.deleteItem(id));
    }

    // 8.
    @DeleteMapping("/restaurant/{restaurantId}")
    public ResponseEntity<String> deleteByRestaurant(
            @PathVariable("restaurantId") Integer restaurantId) {
        return ResponseEntity.ok(menuItemsService.deleteByRestaurant(restaurantId));
    }

    // ---------------- RELATION ----------------

    // 9.
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItems>> getByRestaurant(
            @PathVariable("restaurantId") Integer restaurantId) {
        return ResponseEntity.ok(menuItemsService.getByRestaurantId(restaurantId));
    }

    // 10.
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<MenuItems>> getByOrder(
            @PathVariable("orderId") Integer orderId) {
        return ResponseEntity.ok(menuItemsService.getItemsByOrderId(orderId));
    }

    // ---------------- SEARCH ----------------

    // 11.
    @GetMapping("/name/{name}")
    public ResponseEntity<List<MenuItems>> getByName(
            @PathVariable("name") String name) {
        return ResponseEntity.ok(menuItemsService.getByName(name));
    }

    // ---------------- PRICE FILTERS ----------------

    // 12.
    @GetMapping("/price-range")
    public ResponseEntity<List<MenuItems>> getByPriceRange(
            @RequestParam("min") Double min,
            @RequestParam("max") Double max) {
        return ResponseEntity.ok(menuItemsService.getByPriceRange(min, max));
    }

    // 13.
    @GetMapping("/restaurant/{restaurantId}/price-range")
    public ResponseEntity<List<MenuItems>> getByRestaurantAndPrice(
            @PathVariable("restaurantId") Integer restaurantId,
            @RequestParam("min") Double min,
            @RequestParam("max") Double max) {
        return ResponseEntity.ok(
                menuItemsService.getByRestaurantAndPrice(restaurantId, min, max));
    }

    // ---------------- COMPLEX APIs ----------------

    // 14.
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<MenuItems>> getItemsByCustomer(
            @PathVariable("customerId") Integer customerId) {
        return ResponseEntity.ok(menuItemsService.getItemsByCustomer(customerId));
    }

    // 15.
    @GetMapping("/order-date")
    public ResponseEntity<List<MenuItems>> getItemsByOrderDate(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(menuItemsService.getItemsByOrderDate(date));
    }

    // 16.
    @GetMapping("/top-selling")
    public ResponseEntity<List<MenuItems>> getTopSellingItems() {
        return ResponseEntity.ok(menuItemsService.getTopSellingItems());
    }

    // 17.
    @GetMapping("/rating")
    public ResponseEntity<List<MenuItemWithRating>> getItemsByRating(
            @RequestParam("rating") int rating) {
        return ResponseEntity.ok(menuItemsService.getItemsByRating(rating));
    }

    // 18.
    @GetMapping("/restaurant/{restaurantId}/rating")
    public ResponseEntity<List<MenuItemWithRating>> getItemsByRestaurantAndRating(
            @PathVariable("restaurantId") Integer restaurantId,
            @RequestParam("min") int minRating) {
        return ResponseEntity.ok(
                menuItemsService.getItemsByRestaurantAndRating(restaurantId, minRating));
    }

    // ---------------- SORT ----------------

    // 19.
    @GetMapping("/sort")
    public ResponseEntity<List<MenuItems>> sortItems(
            @RequestParam(value = "field", defaultValue = "itemPrice") String field,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        return ResponseEntity.ok(menuItemsService.sortItems(field, direction));
    }

    // ---------------- PAGINATION ----------------

    // 20.
    @GetMapping("/page")
    public ResponseEntity<PagedResponse<MenuItems>> getPagedItems(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        return ResponseEntity.ok(menuItemsService.getPagedItems(page, size));
    }
}