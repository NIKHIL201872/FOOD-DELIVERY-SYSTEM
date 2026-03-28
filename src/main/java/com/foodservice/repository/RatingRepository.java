package com.foodservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.foodservice.entity.RatingEntity;

public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {

    List<RatingEntity> findByRestaurant_RestaurantId(Integer restaurantId);

    List<RatingEntity> findByOrder_OrderId(Integer orderId);

    List<RatingEntity> findByRating(Integer rating);
}