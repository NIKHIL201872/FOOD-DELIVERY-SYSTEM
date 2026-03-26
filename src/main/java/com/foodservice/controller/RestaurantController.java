package com.foodservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.foodservice.dto.Restaurants;
import com.foodservice.entity.RatingEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.services.RatingService;
import com.foodservice.services.RestaurantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService service;
    private final RatingService ratingService;

    // ✅ CREATE
    @PostMapping
    public RestaurantsEntity create(@RequestBody Restaurants dto) {
        return service.create(dto);
    }

    // ✅ GET ALL + SEARCH (MERGED - BEST PRACTICE)
    @GetMapping
    public List<RestaurantsEntity> getRestaurants(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "phone", required = false) String phone) {

        return service.getRestaurants(name, address, phone);
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public RestaurantsEntity getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public RestaurantsEntity update(@PathVariable Integer id,
                                    @RequestBody Restaurants dto) {
        return service.update(id, dto);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Deleted successfully";
    }

    // 🔥 GET RATINGS OF RESTAURANT
    @GetMapping("/{id}/ratings")
    public List<RatingEntity> getRatings(@PathVariable Integer id) {
        return ratingService.getRatings(id, null, null);
    }
}