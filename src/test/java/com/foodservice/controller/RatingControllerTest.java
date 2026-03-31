package com.foodservice.controller;

import com.foodservice.dto.Rating;
import com.foodservice.dto.RatingOnlyDTO;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.entity.RatingEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.services.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    private Rating sampleRatingDto;
    private RatingEntity sampleRatingEntity;
    private RatingOnlyDTO sampleFlatDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleRatingDto = new Rating(1, 101, 201, 5, "Excellent", null);

        OrdersEntity order = new OrdersEntity();
        order.setOrderId(101);
        RestaurantsEntity restaurant = new RestaurantsEntity();
        restaurant.setRestaurantId(201);

        sampleRatingEntity = new RatingEntity();
        sampleRatingEntity.setRatingId(1);
        sampleRatingEntity.setRating(5);
        sampleRatingEntity.setReview("Excellent");
        sampleRatingEntity.setOrder(order);
        sampleRatingEntity.setRestaurant(restaurant);

        sampleFlatDto = new RatingOnlyDTO(1, 5, "Excellent", 201, 101);
    }

    @Test
    void add_returnsSuccessMessage() {
        when(ratingService.addRating(sampleRatingDto)).thenReturn(sampleRatingEntity);

        String response = ratingController.add(sampleRatingDto);

        assertEquals("Rating created", response);
        verify(ratingService).addRating(sampleRatingDto);
    }

    @Test
    void getAll_returnsRatings() {
        when(ratingService.getAll()).thenReturn(List.of(sampleRatingEntity));

        List<RatingEntity> response = ratingController.getAll();

        assertEquals(1, response.size());
        assertEquals(1, response.get(0).getRatingId());
        verify(ratingService).getAll();
    }

    @Test
    void getById_returnsRating() {
        when(ratingService.getById(1)).thenReturn(sampleRatingEntity);

        RatingEntity response = ratingController.getById(1);

        assertEquals(1, response.getRatingId());
        assertEquals(5, response.getRating());
        verify(ratingService).getById(1);
    }

    @Test
    void getByRestaurant_returnsRatings() {
        when(ratingService.getByRestaurant(201)).thenReturn(List.of(sampleRatingDto));

        List<Rating> response = ratingController.getByRestaurant(201);

        assertEquals(1, response.size());
        assertEquals(201, response.get(0).getRestaurantId());
        verify(ratingService).getByRestaurant(201);
    }

    @Test
    void getByOrder_returnsRatings() {
        when(ratingService.getByOrder(101)).thenReturn(List.of(sampleRatingDto));

        List<Rating> response = ratingController.getByOrder(101);

        assertEquals(1, response.size());
        assertEquals(101, response.get(0).getOrderId());
        verify(ratingService).getByOrder(101);
    }

    @Test
    void getByRating_returnsRatings() {
        when(ratingService.getByRating(5)).thenReturn(List.of(sampleRatingEntity));

        List<RatingEntity> response = ratingController.getByRating(5);

        assertEquals(1, response.size());
        assertEquals(5, response.get(0).getRating());
        verify(ratingService).getByRating(5);
    }

    @Test
    void update_returnsUpdatedRating() {
        when(ratingService.update(1, sampleRatingDto)).thenReturn(sampleRatingEntity);

        RatingEntity response = ratingController.update(1, sampleRatingDto);

        assertEquals(1, response.getRatingId());
        assertEquals("Excellent", response.getReview());
        verify(ratingService).update(1, sampleRatingDto);
    }

    @Test
    void delete_returnsSuccessMessage() {
        doNothing().when(ratingService).delete(1);

        String response = ratingController.delete(1);

        assertEquals("Rating deleted successfully", response);
        verify(ratingService).delete(1);
    }

    @Test
    void getAllFlatRatings_returnsFlatList() {
        when(ratingService.getAllFlat()).thenReturn(List.of(sampleFlatDto));

        List<RatingOnlyDTO> response = ratingController.getAllFlatRatings();

        assertEquals(1, response.size());
        assertEquals(201, response.get(0).getRestaurantId());
        verify(ratingService).getAllFlat();
    }

    @Test
    void getFlatRatingById_returnsFlatRating() {
        when(ratingService.getFlatById(1)).thenReturn(sampleFlatDto);

        RatingOnlyDTO response = ratingController.getFlatRatingById(1);

        assertEquals(1, response.getRatingId());
        assertEquals(101, response.getOrderId());
        verify(ratingService).getFlatById(1);
    }
}
