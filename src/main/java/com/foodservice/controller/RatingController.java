package com.foodservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.foodservice.dto.Rating;
import com.foodservice.entity.RatingEntity;
import com.foodservice.services.RatingService;
import com.foodservice.dto.RatingOnlyDTO;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;
     

    // ✅ CREATE
    @PostMapping
    public String add(@RequestBody Rating dto) {
         ratingService.addRating(dto);
         return "Rating created";
    }

    // ✅ GET ALL
    @GetMapping
    public List<RatingEntity> getAll() {
        return ratingService.getAll();
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public RatingEntity getById(@PathVariable("id") Integer id) {
        return ratingService.getById(id);
    }

    // ✅ GET BY RESTAURANT
    @GetMapping("/restaurant/{restaurantId}")
    public List<Rating> getByRestaurant(@PathVariable("restaurantId") Integer restaurantId) {
        return ratingService.getByRestaurant(restaurantId);
    }

    // ✅ GET BY ORDER
    @GetMapping("/order/{orderId}")
    public List<Rating> getByOrder(@PathVariable("orderId") Integer orderId) {
        return ratingService.getByOrder(orderId);
    }

    // ✅ GET BY RATING VALUE with restaurant menu
    @GetMapping("/value/{rating}")
    public List<RatingEntity> getByRating(@PathVariable("rating") Integer rating) {
        return ratingService.getByRating(rating);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public RatingEntity update(@PathVariable("id") Integer id, @RequestBody Rating dto) {
        return ratingService.update(id, dto);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id) {
        ratingService.delete(id);
        return "Rating deleted successfully";
    }
    @GetMapping("/flat")
    public List<RatingOnlyDTO> getAllFlatRatings() {
        return ratingService.getAllFlat();
    }
    @GetMapping("/flat/{id}")
    public RatingOnlyDTO getFlatRatingById(@PathVariable Integer id) {
        return ratingService.getFlatById(id);
    }
}