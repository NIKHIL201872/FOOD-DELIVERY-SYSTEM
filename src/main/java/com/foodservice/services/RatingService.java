package com.foodservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodservice.DAO.RatingDAO;
import com.foodservice.DAO.RestaurantDAO;
import com.foodservice.dto.Rating;
import com.foodservice.dto.RatingOnlyDTO;
import com.foodservice.dto.Restaurants;
import com.foodservice.entity.RatingEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.entity.MenuItemsEntity;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.exception.ResourceNotFoundException;
import com.foodservice.repository.OrdersRepository;
import com.foodservice.repository.RatingRepository;
import com.foodservice.exception.BadRequestException;
//import com.foodservice.repository.OrdersRepository;

@Service
public class RatingService {

    @Autowired
    private RatingDAO ratingDAO;

    @Autowired
    private RestaurantDAO restaurantDAO;

    @Autowired
    private OrdersRepository ordersRepo;
   
    @Autowired
    private RatingRepository ratingrepo;
    //  CREATE
    public RatingEntity addRating(Rating dto) {

        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }

        RestaurantsEntity restaurant = restaurantDAO.findById(dto.getRestaurantId());
        if (restaurant == null) {
            throw new ResourceNotFoundException("Restaurant not found with ID: " + dto.getRestaurantId());
        }

        OrdersEntity order = ordersRepo.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + dto.getOrderId()));

        RatingEntity rating = new RatingEntity();
        rating.setRating(dto.getRating());
        rating.setReview(dto.getReview());
        rating.setRestaurant(restaurant);
        rating.setOrder(order);

        return ratingDAO.save(rating);
    }

    //  GET ALL
    public List<RatingEntity> getAll() {
        return ratingDAO.findAll();
    }

    //  GET BY ID
    public RatingEntity getById(Integer id) {
        RatingEntity rating = ratingDAO.findById(id);

        if (rating == null) {
            throw new ResourceNotFoundException("Rating not found with ID: " + id);
        }

        return rating;
    }

    //  GET BY RESTAURANT
    public List<Rating> getByRestaurant(Integer restaurantId) {

        List<RatingEntity> list =
                ratingDAO.findByRestaurantId(restaurantId);

        return list.stream().map(entity -> {

            Rating dto = new Rating();

            dto.setRatingId(entity.getRatingId());
            dto.setRating(entity.getRating());
            dto.setReview(entity.getReview());

            // map restaurant WITHOUT menuItems
            Restaurants resDto = new Restaurants();
            resDto.setRestaurantId(entity.getRestaurant().getRestaurantId());
            resDto.setRestaurantName(entity.getRestaurant().getRestaurantName());
            resDto.setRestaurantAddress(entity.getRestaurant().getRestaurantAddress());
            resDto.setRestaurantPhone(entity.getRestaurant().getRestaurantPhone());

            dto.setRestaurants(resDto);

            return dto;

        }).toList();
    }
    //  GET BY RESTAURANT with menu
    public List<RatingEntity> getByRestaurantwithmenu(Integer restaurantId) {
        return ratingDAO.findByRestaurantId(restaurantId);
    }
    //  GET BY ORDER
    public List<Rating> getByOrder(Integer orderId) {
        List<RatingEntity> list = ratingrepo.findByOrder_OrderId(orderId);

        return list.stream().map(entity -> {
        	Rating dto = new Rating();
            dto.setRatingId(entity.getRatingId());
            dto.setRating(entity.getRating());
            dto.setReview(entity.getReview());
            dto.setRestaurantId(entity.getRestaurant().getRestaurantId());
            dto.setOrderId(entity.getOrder().getOrderId()); // only ID
            return dto;
        }).toList();
    }

    //  GET BY RATING VALUE
    public List<RatingEntity> getByRating(Integer rating) {
        return ratingDAO.findByRating(rating);
    }

    //  UPDATE
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

    //  DELETE
    public void delete(Integer id) {
        RatingEntity rating = getById(id);
        ratingDAO.delete(rating);
    }
    public List<RatingOnlyDTO> getAllFlat() {
        List<RatingEntity> list = ratingrepo.findAll();

        return list.stream().map(entity -> {
        	RatingOnlyDTO dto = new RatingOnlyDTO();
            dto.setRatingId(entity.getRatingId());
            dto.setRating(entity.getRating());
            dto.setReview(entity.getReview());

            // extract only IDs
            dto.setRestaurantId(entity.getRestaurant().getRestaurantId());
            dto.setOrderId(entity.getOrder().getOrderId());

            return dto;
        }).toList();
    }
    public RatingOnlyDTO getFlatById(Integer id) {
        RatingEntity entity = ratingrepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Rating not found"));

        RatingOnlyDTO dto = new RatingOnlyDTO();
        dto.setRatingId(entity.getRatingId());
        dto.setRating(entity.getRating());
        dto.setReview(entity.getReview());

        // extract only IDs (no full objects)
        dto.setRestaurantId(
            entity.getRestaurant() != null ? entity.getRestaurant().getRestaurantId() : null
        );

        dto.setOrderId(
            entity.getOrder() != null ? entity.getOrder().getOrderId() : null
        );

        return dto;
    }
}