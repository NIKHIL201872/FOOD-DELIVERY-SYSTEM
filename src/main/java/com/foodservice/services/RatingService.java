package com.foodservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodservice.DAO.RatingDAO;
import com.foodservice.DAO.RestaurantDAO;
import com.foodservice.dto.Rating;
import com.foodservice.entity.RatingEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.exception.ResourceNotFoundException;
import com.foodservice.exception.BadRequestException;
//import com.foodservice.repository.OrdersRepository;

@Service
public class RatingService {

    @Autowired
    private RatingDAO ratingDAO;

    @Autowired
    private RestaurantDAO restaurantDAO;

//    @Autowired
//    private OrdersRepository ordersRepo;

    // ✅ CREATE
//    public RatingEntity addRating(Rating dto) {
//
//        if (dto.getRating() < 1 || dto.getRating() > 5) {
//            throw new BadRequestException("Rating must be between 1 and 5");
//        }
//
//        RestaurantsEntity restaurant = restaurantDAO.findById(dto.getRestaurantId());
//        if (restaurant == null) {
//            throw new ResourceNotFoundException("Restaurant not found with ID: " + dto.getRestaurantId());
//        }
//
//        OrdersEntity order = ordersRepo.findById(dto.getOrderId())
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + dto.getOrderId()));
//
//        RatingEntity rating = new RatingEntity();
//        rating.setRating(dto.getRating());
//        rating.setReview(dto.getReview());
//        rating.setRestaurant(restaurant);
//        rating.setOrder(order);
//
//        return ratingDAO.save(rating);
//    }

    // ✅ GET ALL
    public List<RatingEntity> getAll() {
        return ratingDAO.findAll();
    }

    // ✅ GET BY ID
    public RatingEntity getById(Integer id) {
        RatingEntity rating = ratingDAO.findById(id);

        if (rating == null) {
            throw new ResourceNotFoundException("Rating not found with ID: " + id);
        }

        return rating;
    }

    // ✅ GET BY RESTAURANT
    public List<RatingEntity> getByRestaurant(Integer restaurantId) {
        return ratingDAO.findByRestaurantId(restaurantId);
    }

    // ✅ GET BY ORDER
    public List<RatingEntity> getByOrder(Integer orderId) {
        return ratingDAO.findByOrderId(orderId);
    }

    // ✅ GET BY RATING VALUE
    public List<RatingEntity> getByRating(Integer rating) {
        return ratingDAO.findByRating(rating);
    }

    // ✅ UPDATE
    public RatingEntity update(Integer id, Rating dto) {

        RatingEntity rating = getById(id);

        if (dto.getRating() != null) {
            if (dto.getRating() < 1 || dto.getRating() > 5) {
                throw new BadRequestException("Rating must be between 1 and 5");
            }
            rating.setRating(dto.getRating());
        }

        if (dto.getReview() != null) {
            rating.setReview(dto.getReview());
        }

        return ratingDAO.save(rating);
    }

    // ✅ DELETE
    public void delete(Integer id) {
        RatingEntity rating = getById(id);
        ratingDAO.delete(rating);
    }
}