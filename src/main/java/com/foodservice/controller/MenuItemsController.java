package com.foodservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodservice.dto.MenuItems;
import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.services.MenuItemsService;

@RestController
@RequestMapping("/menuitems")
public class MenuItemsController {

    @Autowired
    private MenuItemsService menuItemsService;

    // ---------------- CREATE ----------------

    @PostMapping
    public ResponseEntity<MenuItems> createItem(@RequestBody MenuItems item) {
        return new ResponseEntity<>(menuItemsService.saveItem(item), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<MenuItems>> saveAll(@RequestBody List<MenuItemsEntity> items) {
        return new ResponseEntity<>(menuItemsService.saveAll(items), HttpStatus.CREATED);
    }

    // ---------------- READ ----------------

    @GetMapping
    public ResponseEntity<List<MenuItems>> getAllItems() {
        return ResponseEntity.ok(menuItemsService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItems> getItemById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(menuItemsService.getItemById(id));
    }

    // ---------------- EXTRA ----------------

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok((long) menuItemsService.getAllItems().size());
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> exists(@PathVariable("id") Integer id) {
        try {
            menuItemsService.getItemById(id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    // ---------------- UPDATE ----------------

    @PutMapping("/{id}")
    public ResponseEntity<MenuItems> updateItem(
            @PathVariable("id") Integer id,
            @RequestBody MenuItemsEntity item) {
        return ResponseEntity.ok(menuItemsService.updateItem(id, item));
    }

    // ---------------- DELETE ----------------

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable("id") Integer id) {
        menuItemsService.deleteItem(id);
        return ResponseEntity.ok("Item deleted successfully");
    }

    @DeleteMapping("/restaurant/{restaurantId}")
    public ResponseEntity<String> deleteByRestaurant(
            @PathVariable("restaurantId") Integer restaurantId) {
        menuItemsService.deleteByRestaurant(restaurantId);
        return ResponseEntity.ok("Deleted all items of restaurant");
    }

    // ---------------- RELATION ----------------

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItems>> getByRestaurant(
            @PathVariable("restaurantId") Integer restaurantId) {
        return ResponseEntity.ok(menuItemsService.getByRestaurantId(restaurantId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<MenuItems>> getByOrder(
            @PathVariable("orderId") Integer orderId) {
        return ResponseEntity.ok(menuItemsService.getItemsByOrderId(orderId));
    }

    // ---------------- SEARCH ----------------

    @GetMapping("/name/{name}")
    public ResponseEntity<List<MenuItems>> getByName(
            @PathVariable("name") String name) {
        return ResponseEntity.ok(menuItemsService.getByName(name));
    }

    // ---------------- PRICE FILTERS ----------------

    @GetMapping("/min-price")
    public ResponseEntity<List<MenuItems>> getMinPriceItems() {
        return ResponseEntity.ok(menuItemsService.sortItems("itemPrice", "asc"));
    }

    @GetMapping("/max-price")
    public ResponseEntity<List<MenuItems>> getMaxPriceItems() {
        return ResponseEntity.ok(menuItemsService.sortItems("itemPrice", "desc"));
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<MenuItems>> getByPriceRange(
            @RequestParam("min") Double min,
            @RequestParam("max") Double max) {
        return ResponseEntity.ok(menuItemsService.getByPriceRange(min, max));
    }

    @GetMapping("/restaurant/{restaurantId}/price-range")
    public ResponseEntity<List<MenuItems>> getByRestaurantAndPrice(
            @PathVariable("restaurantId") Integer restaurantId,
            @RequestParam("min") Double min,
            @RequestParam("max") Double max) {
        return ResponseEntity.ok(
                menuItemsService.getByRestaurantAndPrice(restaurantId, min, max));
    }

    // ---------------- COMPLEX APIs ----------------

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<MenuItems>> getItemsByCustomer(
            @PathVariable("customerId") Integer customerId) {
        return ResponseEntity.ok(menuItemsService.getItemsByCustomer(customerId));
    }

    @GetMapping("/order-date")
    public ResponseEntity<List<MenuItems>> getItemsByOrderDate(
            @RequestParam("date") String date) {
        return ResponseEntity.ok(menuItemsService.getItemsByOrderDate(date));
    }

    @GetMapping("/top-selling")
    public ResponseEntity<List<MenuItems>> getTopSellingItems() {
        return ResponseEntity.ok(menuItemsService.getTopSellingItems());
    }

    @GetMapping("/rating")
    public ResponseEntity<List<MenuItems>> getItemsByRating(
            @RequestParam("rating") int rating) {
        return ResponseEntity.ok(menuItemsService.getItemsByRating(rating));
    }

    // ---------------- SORT ----------------

    @GetMapping("/sort")
    public ResponseEntity<List<MenuItems>> sortItems(
            @RequestParam(value = "field", defaultValue = "itemPrice") String field,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        return ResponseEntity.ok(menuItemsService.sortItems(field, direction));
    }

    // ---------------- PAGINATION ----------------

    @GetMapping("/page")
    public ResponseEntity<Page<?>> getPagedItems(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        return ResponseEntity.ok(menuItemsService.getPagedItems(page, size));
    }
}