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


    @PostMapping
    public RestaurantsEntity create(@RequestBody Restaurants dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<RestaurantsEntity> getAll() {
        return service.getAll();
    }

    @GetMapping("/id/{id}")
    public RestaurantsEntity getById(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @GetMapping("/search/name")
    public List<RestaurantsEntity> searchByName(@RequestParam("name") String name) {
        return service.searchByName(name);
    }

    @GetMapping("/search/address")
    public List<RestaurantsEntity> searchByAddress(@RequestParam("address") String address) {
        return service.searchByAddress(address);
    }

    @GetMapping("/search/phone")
    public List<RestaurantsEntity> searchByPhone(@RequestParam("phone") String phone) {
        return service.searchByPhone(phone);
    }

    @PutMapping("/{id}")
    public RestaurantsEntity update(@PathVariable("id") Integer id,
                                    @RequestBody Restaurants dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return "Restaurant deleted successfully";
    }

    @GetMapping("/{id}/ratings")
    public List<RatingEntity> getRatings(@PathVariable("id") Integer id) {
        return ratingService.getByRestaurant(id);
    }
}