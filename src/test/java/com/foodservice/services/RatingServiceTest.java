package com.foodservice.services;

import com.foodservice.DAO.RatingDAO;
import com.foodservice.DAO.RestaurantDAO;
import com.foodservice.dto.Rating;
import com.foodservice.dto.RatingOnlyDTO;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.entity.RatingEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.exception.BadRequestException;
import com.foodservice.exception.ResourceNotFoundException;
import com.foodservice.repository.OrdersRepository;
import com.foodservice.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RatingServiceTest {

    @Mock
    private RatingDAO ratingDAO;

    @Mock
    private RestaurantDAO restaurantDAO;

    @Mock
    private OrdersRepository ordersRepo;

    @Mock
    private RatingRepository ratingrepo;

    @InjectMocks
    private RatingService service;

    private Rating ratingDto;
    private RatingEntity ratingEntity;
    private RestaurantsEntity restaurant;
    private OrdersEntity order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ratingDto = new Rating(1, 101, 201, 5, "Excellent", null);

        restaurant = new RestaurantsEntity();
        restaurant.setRestaurantId(201);
        restaurant.setRestaurantName("Spice Hub");
        restaurant.setRestaurantAddress("MG Road");
        restaurant.setRestaurantPhone("9999999999");

        order = new OrdersEntity();
        order.setOrderId(101);

        ratingEntity = new RatingEntity();
        ratingEntity.setRatingId(1);
        ratingEntity.setRating(5);
        ratingEntity.setReview("Excellent");
        ratingEntity.setRestaurant(restaurant);
        ratingEntity.setOrder(order);
    }

    @Test
    void addRating_savesAndReturnsEntity() {
        when(restaurantDAO.findById(201)).thenReturn(restaurant);
        when(ordersRepo.findById(101)).thenReturn(Optional.of(order));
        when(ratingDAO.save(any(RatingEntity.class))).thenReturn(ratingEntity);

        RatingEntity result = service.addRating(ratingDto);

        assertEquals(1, result.getRatingId());
        assertEquals(5, result.getRating());
        verify(ratingDAO).save(any(RatingEntity.class));
    }

    @Test
    void addRating_throwsForInvalidRatingValue() {
        ratingDto.setRating(0);

        assertThrows(BadRequestException.class, () -> service.addRating(ratingDto));
        verify(ratingDAO, never()).save(any(RatingEntity.class));
    }

    @Test
    void getById_returnsRating() {
        when(ratingDAO.findById(1)).thenReturn(ratingEntity);

        RatingEntity result = service.getById(1);

        assertEquals(1, result.getRatingId());
        assertEquals("Excellent", result.getReview());
    }

    @Test
    void getById_throwsWhenMissing() {
        when(ratingDAO.findById(99)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99));
    }

    @Test
    void getByRestaurant_returnsMappedDtos() {
        when(ratingDAO.findByRestaurantId(201)).thenReturn(List.of(ratingEntity));

        List<Rating> result = service.getByRestaurant(201);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getRatingId());
        assertEquals("Spice Hub", result.get(0).getRestaurants().getRestaurantName());
    }

    @Test
    void getByOrder_returnsMappedDtos() {
        when(ratingrepo.findByOrder_OrderId(101)).thenReturn(List.of(ratingEntity));

        List<Rating> result = service.getByOrder(101);

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getOrderId());
        assertEquals(201, result.get(0).getRestaurantId());
    }

    @Test
    void update_updatesFieldsAndSaves() {
        Rating update = new Rating();
        update.setRating(4);
        update.setReview("Good");
        when(ratingDAO.findById(1)).thenReturn(ratingEntity);
        when(ratingDAO.save(ratingEntity)).thenReturn(ratingEntity);

        RatingEntity result = service.update(1, update);

        assertEquals(4, result.getRating());
        assertEquals("Good", result.getReview());
        verify(ratingDAO).save(ratingEntity);
    }

    @Test
    void delete_removesRating() {
        when(ratingDAO.findById(1)).thenReturn(ratingEntity);
        doNothing().when(ratingDAO).delete(ratingEntity);

        service.delete(1);

        verify(ratingDAO).delete(ratingEntity);
    }

    @Test
    void getAllFlat_returnsFlatDtos() {
        when(ratingrepo.findAll()).thenReturn(List.of(ratingEntity));

        List<RatingOnlyDTO> result = service.getAllFlat();

        assertEquals(1, result.size());
        assertEquals(201, result.get(0).getRestaurantId());
        assertEquals(101, result.get(0).getOrderId());
    }

    @Test
    void getFlatById_returnsFlatDto() {
        when(ratingrepo.findById(1)).thenReturn(Optional.of(ratingEntity));

        RatingOnlyDTO result = service.getFlatById(1);

        assertEquals(1, result.getRatingId());
        assertEquals(5, result.getRating());
        assertEquals(201, result.getRestaurantId());
    }
}
