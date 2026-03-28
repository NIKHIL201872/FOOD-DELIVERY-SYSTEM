package com.foodservice.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.foodservice.DAO.RestaurantDAO;
import com.foodservice.dto.Restaurants;
import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantDAO dao;
    public RestaurantsEntity create(Restaurants dto) {

        if (dto.getRestaurantName() == null) {
            throw new IllegalArgumentException("Restaurant name is required");
        }

        RestaurantsEntity entity = new RestaurantsEntity();
        entity.setRestaurantName(dto.getRestaurantName());
        entity.setRestaurantAddress(dto.getRestaurantAddress());
        entity.setRestaurantPhone(dto.getRestaurantPhone());

        return dao.save(entity);
    }

    public List<RestaurantsEntity> getAll() {
        List<RestaurantsEntity> list = dao.findAll();

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No restaurants available");
        }

        return list;
    }

    public RestaurantsEntity getById(Integer id) {
        RestaurantsEntity r = dao.findById(id);

        if (r == null) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + id);
        }

        return r;
    }

    public List<RestaurantsEntity> searchByName(String name) {
        List<RestaurantsEntity> list = dao.findByName(name);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No restaurants found with name: " + name);
        }

        return list;
    }

    public List<RestaurantsEntity> searchByAddress(String address) {
        List<RestaurantsEntity> list = dao.findByAddress(address);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No restaurants found with address: " + address);
        }

        return list;
    }

    public List<RestaurantsEntity> searchByPhone(String phone) {
        List<RestaurantsEntity> list = dao.findByPhone(phone);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No restaurants found with phone: " + phone);
        }

        return list;
    }

    public RestaurantsEntity update(Integer id, Restaurants dto) {

        RestaurantsEntity entity = getById(id);

        entity.setRestaurantName(dto.getRestaurantName());
        entity.setRestaurantAddress(dto.getRestaurantAddress());
        entity.setRestaurantPhone(dto.getRestaurantPhone());

        return dao.save(entity);
    }

    public void delete(Integer id) {

        RestaurantsEntity entity = getById(id);
        dao.delete(entity);
    }
}