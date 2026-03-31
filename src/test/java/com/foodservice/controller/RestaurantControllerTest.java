package com.foodservice.controller;

import com.foodservice.dto.MenuItems;
import com.foodservice.dto.Restaurants;
import com.foodservice.entity.RatingEntity;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.services.MenuItemsService;
import com.foodservice.services.RatingService;
import com.foodservice.services.RestaurantService;
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

class RestaurantControllerTest {

    @Mock
    private RestaurantService service;

    @Mock
    private RatingService ratingService;

    @Mock
    private MenuItemsService menuItemsService;

    @InjectMocks
    private RestaurantController controller;

    private Restaurants sampleDto;
    private RestaurantsEntity sampleEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleDto = new Restaurants(1, "Spice Hub", "MG Road", "9876543210");

        sampleEntity = new RestaurantsEntity();
        sampleEntity.setRestaurantId(1);
        sampleEntity.setRestaurantName("Spice Hub");
        sampleEntity.setRestaurantAddress("MG Road");
        sampleEntity.setRestaurantPhone("9876543210");
    }

    @Test
    void create_returnsRestaurantEntity() {
        when(service.create(sampleDto)).thenReturn(sampleEntity);

        RestaurantsEntity response = controller.create(sampleDto);

        assertEquals(1, response.getRestaurantId());
        assertEquals("Spice Hub", response.getRestaurantName());
        verify(service).create(sampleDto);
    }

    @Test
    void getAll_returnsRestaurantList() {
        when(service.getAll()).thenReturn(List.of(sampleEntity));

        List<RestaurantsEntity> response = controller.getAll();

        assertEquals(1, response.size());
        assertEquals(1, response.get(0).getRestaurantId());
        verify(service).getAll();
    }

    @Test
    void getById_returnsRestaurant() {
        when(service.getById(1)).thenReturn(sampleEntity);

        RestaurantsEntity response = controller.getById(1);

        assertEquals(1, response.getRestaurantId());
        assertEquals("MG Road", response.getRestaurantAddress());
        verify(service).getById(1);
    }

    @Test
    void searchByName_returnsResults() {
        when(service.searchByName("Spice")).thenReturn(List.of(sampleEntity));

        List<RestaurantsEntity> response = controller.searchByName("Spice");

        assertEquals(1, response.size());
        verify(service).searchByName("Spice");
    }

    @Test
    void searchByAddress_returnsResults() {
        when(service.searchByAddress("MG Road")).thenReturn(List.of(sampleEntity));

        List<RestaurantsEntity> response = controller.searchByAddress("MG Road");

        assertEquals(1, response.size());
        verify(service).searchByAddress("MG Road");
    }

    @Test
    void searchByPhone_returnsResults() {
        when(service.searchByPhone("9876543210")).thenReturn(List.of(sampleEntity));

        List<RestaurantsEntity> response = controller.searchByPhone("9876543210");

        assertEquals(1, response.size());
        verify(service).searchByPhone("9876543210");
    }

    @Test
    void update_returnsUpdatedRestaurant() {
        when(service.update(1, sampleDto)).thenReturn(sampleEntity);

        RestaurantsEntity response = controller.update(1, sampleDto);

        assertEquals(1, response.getRestaurantId());
        assertEquals("Spice Hub", response.getRestaurantName());
        verify(service).update(1, sampleDto);
    }

    @Test
    void delete_returnsSuccessMessage() {
        doNothing().when(service).delete(1);

        String response = controller.delete(1);

        assertEquals("Restaurant deleted successfully", response);
        verify(service).delete(1);
    }

    @Test
    void getRatings_returnsRatingList() {
        RatingEntity rating = new RatingEntity();
        rating.setRatingId(11);
        rating.setRating(5);
        when(ratingService.getByRestaurantwithmenu(1)).thenReturn(List.of(rating));

        List<RatingEntity> response = controller.getRatings(1);

        assertEquals(1, response.size());
        assertEquals(11, response.get(0).getRatingId());
        verify(ratingService).getByRestaurantwithmenu(1);
    }

    @Test
    void getMenuItemsByRestaurantId_returnsMenuItems() {
        MenuItems item = new MenuItems(101, "Paneer Tikka", "Starter", 299.0, 1);
        when(menuItemsService.getByRestaurantId(1)).thenReturn(List.of(item));

        List<MenuItems> response = controller.getMenuItemsByRestaurantId(1);

        assertEquals(1, response.size());
        assertEquals(101, response.get(0).getItemId());
        verify(menuItemsService).getByRestaurantId(1);
    }
}
