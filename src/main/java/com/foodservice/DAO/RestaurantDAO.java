package com.foodservice.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.foodservice.entity.RestaurantsEntity;
import com.foodservice.repository.RestaurantsRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RestaurantDAO {

    private final RestaurantsRepository repo;

    public RestaurantsEntity save(RestaurantsEntity restaurant) {
        return repo.save(restaurant);
    }

    public List<RestaurantsEntity> findAll() {
        return repo.findAll();
    }

    public RestaurantsEntity findById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(RestaurantsEntity restaurant) {
        repo.delete(restaurant);
    }

    public List<RestaurantsEntity> findDynamic(String name, String address, String phone) {

        if (name != null) {
            return repo.findByRestaurantNameContainingIgnoreCase(name);
        }

        if (address != null) {
            return repo.findByRestaurantAddressContainingIgnoreCase(address);
        }

        if (phone != null) {
            return repo.findByRestaurantPhone(phone);
        }

        return repo.findAll();
    }
}