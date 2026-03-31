package com.foodservice.services;

import com.foodservice.DAO.RestaurantDAO;
import com.foodservice.dto.Restaurants;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantServiceTest {

    @Mock
    private RestaurantDAO dao;

    @InjectMocks
    private RestaurantService service;

    private Restaurants dto;
    private RestaurantsEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new Restaurants(1, "Spice Hub", "MG Road", "9876543210");

        entity = new RestaurantsEntity();
        entity.setRestaurantId(1);
        entity.setRestaurantName("Spice Hub");
        entity.setRestaurantAddress("MG Road");
        entity.setRestaurantPhone("9876543210");
    }

    @Test
    void create_returnsSavedRestaurant() {
        when(dao.save(org.mockito.ArgumentMatchers.any(RestaurantsEntity.class))).thenReturn(entity);

        RestaurantsEntity result = service.create(dto);

        assertEquals(1, result.getRestaurantId());
        assertEquals("Spice Hub", result.getRestaurantName());
        verify(dao).save(org.mockito.ArgumentMatchers.any(RestaurantsEntity.class));
    }

    @Test
    void create_throwsWhenNameMissing() {
        Restaurants invalid = new Restaurants(1, null, "MG Road", "9876543210");

        assertThrows(IllegalArgumentException.class, () -> service.create(invalid));
    }

    @Test
    void getAll_returnsList() {
        when(dao.findAll()).thenReturn(List.of(entity));

        List<RestaurantsEntity> result = service.getAll();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getRestaurantId());
    }

    @Test
    void getAll_throwsWhenEmpty() {
        when(dao.findAll()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> service.getAll());
    }

    @Test
    void getById_returnsRestaurant() {
        when(dao.findById(1)).thenReturn(entity);

        RestaurantsEntity result = service.getById(1);

        assertEquals(1, result.getRestaurantId());
        assertEquals("MG Road", result.getRestaurantAddress());
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(dao.findById(99)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99));
    }

    @Test
    void searchByName_returnsList() {
        when(dao.findByName("Spice")).thenReturn(List.of(entity));

        List<RestaurantsEntity> result = service.searchByName("Spice");

        assertEquals(1, result.size());
        verify(dao).findByName("Spice");
    }

    @Test
    void searchByName_throwsWhenNoData() {
        when(dao.findByName("Unknown")).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> service.searchByName("Unknown"));
    }

    @Test
    void searchByAddress_returnsList() {
        when(dao.findByAddress("MG Road")).thenReturn(List.of(entity));

        List<RestaurantsEntity> result = service.searchByAddress("MG Road");

        assertEquals(1, result.size());
        verify(dao).findByAddress("MG Road");
    }

    @Test
    void searchByAddress_throwsWhenNoData() {
        when(dao.findByAddress("No Address")).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> service.searchByAddress("No Address"));
    }

    @Test
    void searchByPhone_returnsList() {
        when(dao.findByPhone("9876")).thenReturn(List.of(entity));

        List<RestaurantsEntity> result = service.searchByPhone("9876");

        assertEquals(1, result.size());
        verify(dao).findByPhone("9876");
    }

    @Test
    void searchByPhone_throwsWhenNoData() {
        when(dao.findByPhone("0000")).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> service.searchByPhone("0000"));
    }

    @Test
    void update_returnsUpdatedRestaurant() {
        Restaurants updatedDto = new Restaurants(1, "Spice Hub Updated", "Park Street", "9999999999");
        when(dao.findById(1)).thenReturn(entity);
        when(dao.save(entity)).thenReturn(entity);

        RestaurantsEntity result = service.update(1, updatedDto);

        assertEquals("Spice Hub Updated", result.getRestaurantName());
        assertEquals("Park Street", result.getRestaurantAddress());
        assertEquals("9999999999", result.getRestaurantPhone());
    }

    @Test
    void delete_callsDaoDelete() {
        when(dao.findById(1)).thenReturn(entity);
        doNothing().when(dao).delete(entity);

        service.delete(1);

        verify(dao).delete(entity);
    }
}
