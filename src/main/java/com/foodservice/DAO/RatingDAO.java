package com.foodservice.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    // 🔥 Dynamic Filter
    public List<RatingEntity> findDynamic(Integer restaurantId, Integer orderId, Integer rating) {

        if (restaurantId != null) {
            return repo.findByRestaurant_RestaurantId(restaurantId);
        }

        if (orderId != null) {
            return repo.findByOrder_OrderId(orderId);
        }

        if (rating != null) {
            return repo.findByRating(rating);
        }

        return repo.findAll();
    }
}