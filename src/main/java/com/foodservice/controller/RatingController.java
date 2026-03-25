package com.foodservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodservice.dto.Rating;
import com.foodservice.entity.RatingEntity;
import com.foodservice.services.RatingService;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
	
	@Autowired
     RatingService ratingService;

    // ✅ CREATE
//    @PostMapping
//    public RatingEntity add(@RequestBody Rating dto) {
//        return ratingService.addRating(dto);
//    }

    // ✅ DYNAMIC GET
    @GetMapping
    public List<RatingEntity> getRatings(
            @RequestParam(required = false) Integer restaurantId,
            @RequestParam(required = false) Integer orderId,
            @RequestParam(required = false) Integer rating) {

        return ratingService.getRatings(restaurantId, orderId, rating);
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public RatingEntity getById(@PathVariable Integer id) {
        return ratingService.getById(id);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
//    public RatingEntity update(@PathVariable Integer id, @RequestBody Rating dto) {
//        return ratingService.update(id, dto);
//    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        ratingService.delete(id);
        return "Rating deleted successfully";
    }
}