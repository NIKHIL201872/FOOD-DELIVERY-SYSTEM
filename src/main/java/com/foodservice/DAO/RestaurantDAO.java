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

    public RestaurantsEntity save(RestaurantsEntity entity) {
        return repo.save(entity);
    }

    public List<RestaurantsEntity> findAll() {
        return repo.findAll();
    }

    public RestaurantsEntity findById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(RestaurantsEntity entity) {
        repo.delete(entity);
    }

    public List<RestaurantsEntity> findByName(String name) {
        return repo.findByRestaurantNameContainingIgnoreCase(name);
    }

    public List<RestaurantsEntity> findByAddress(String address) {
        return repo.findByRestaurantAddressContainingIgnoreCase(address);
    }

    public List<RestaurantsEntity> findByPhone(String phone) {
        return repo.findByRestaurantPhoneContaining(phone);
    }
}