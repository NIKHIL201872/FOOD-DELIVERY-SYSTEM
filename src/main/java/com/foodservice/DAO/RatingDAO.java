package com.foodservice.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.foodservice.entity.RatingEntity;
import com.foodservice.repository.RatingRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RatingDAO {

    private final RatingRepository repo;

    public RatingEntity save(RatingEntity rating) {
        return repo.save(rating);
    }

    public List<RatingEntity> findAll() {
        return repo.findAll();
    }

    public RatingEntity findById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(RatingEntity rating) {
        repo.delete(rating);
    }

    public List<RatingEntity> findByRestaurantId(Integer restaurantId) {
        return repo.findByRestaurant_RestaurantId(restaurantId);
    }

    public List<RatingEntity> findByOrderId(Integer orderId) {
        return repo.findByOrder_OrderId(orderId);
    }

    public List<RatingEntity> findByRating(Integer rating) {
        return repo.findByRating(rating);
    }
}