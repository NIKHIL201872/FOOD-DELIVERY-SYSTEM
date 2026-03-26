package com.foodservice.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foodservice.DAO.RestaurantDAO;
import com.foodservice.dto.Restaurants;
import com.foodservice.entity.RestaurantsEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantDAO dao;

    // ✅ CREATE
    public RestaurantsEntity create(Restaurants dto) {
        RestaurantsEntity r = new RestaurantsEntity();
        r.setRestaurantName(dto.getRestaurantName());
        r.setRestaurantAddress(dto.getRestaurantAddress());
        r.setRestaurantPhone(dto.getRestaurantPhone());
        return dao.save(r);
    }

    // ✅ DYNAMIC GET
    public List<RestaurantsEntity> getRestaurants(String name, String address, String phone) {
        return dao.findDynamic(name, address, phone);
    }

    // ✅ GET BY ID
    public RestaurantsEntity getById(Integer id) {
        RestaurantsEntity r = dao.findById(id);
        if (r == null) {
            throw new RuntimeException("Restaurant not found");
        }
        return r;
    }

    // ✅ UPDATE
    public RestaurantsEntity update(Integer id, Restaurants dto) {
        RestaurantsEntity r = getById(id);

        r.setRestaurantName(dto.getRestaurantName());
        r.setRestaurantAddress(dto.getRestaurantAddress());
        r.setRestaurantPhone(dto.getRestaurantPhone());

        return dao.save(r);
    }

    // ✅ DELETE
    public void delete(Integer id) {
        RestaurantsEntity r = getById(id);
        dao.delete(r);
    }
}