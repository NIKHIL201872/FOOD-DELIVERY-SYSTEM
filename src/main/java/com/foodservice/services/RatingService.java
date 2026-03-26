package com.foodservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodservice.DAO.RatingDAO;
import com.foodservice.DAO.RestaurantDAO;
import com.foodservice.dto.Rating;
import com.foodservice.entity.RatingEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service

public class RatingService {

	@Autowired
    RatingDAO ratingDAO;
	@Autowired
    RestaurantDAO restaurantDAO;
//	@Autowired
//    OrdersRepository ordersRepo;

    // ✅ CREATE
//    public RatingEntity addRating(Rating dto) {
//
//        RestaurantsEntity restaurant = restaurantDAO.findById(dto.getRestaurantId());
//        if (restaurant == null) {
//            throw new RuntimeException("Restaurant not found");
//        }
//
//        OrdersEntity order = ordersRepo.findById(dto.getOrderId())
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        RatingEntity rating = new RatingEntity();
//        rating.setRating(dto.getRating());
//        rating.setReview(dto.getReview());
//        rating.setRestaurant(restaurant);
//        rating.setOrder(order);
//
//        return ratingDAO.save(rating);
//    }

    // ✅ GET ALL / DYNAMIC
    public List<RatingEntity> getRatings(Integer restaurantId, Integer orderId, Integer rating) {
        return ratingDAO.findDynamic(restaurantId, orderId, rating);
    }

    // ✅ GET BY ID
    public RatingEntity getById(Integer id) {
        RatingEntity rating = ratingDAO.findById(id);
        if (rating == null) {
            throw new RuntimeException("Rating not found");
        }
        return rating;
    }

//    // ✅ UPDATE
//    public RatingEntity update(Integer id, Rating dto) {
//        RatingEntity rating = getById(id);
//
//        rating.setRating(dto.getRating());
//        rating.setReview(dto.getReview());
//
//        if (dto.getOrderId() != null) {
//            OrdersEntity order = ordersRepo.findById(dto.getOrderId())
//                    .orElseThrow(() -> new RuntimeException("Order not found"));
//            rating.setOrder(order);
//        }
//
//        return ratingDAO.save(rating);
//    }

    // ✅ DELETE
    public void delete(Integer id) {
        RatingEntity rating = getById(id);
        ratingDAO.delete(rating);
    }
}