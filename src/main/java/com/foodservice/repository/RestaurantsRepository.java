package com.foodservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.foodservice.entity.RestaurantsEntity;

public interface RestaurantsRepository extends JpaRepository<RestaurantsEntity, Integer> {

   // RestaurantsEntity findByRestaurantName(String name);
	 List<RestaurantsEntity> findByRestaurantNameContainingIgnoreCase(String name);

	    List<RestaurantsEntity> findByRestaurantAddressContainingIgnoreCase(String address);

	    List<RestaurantsEntity> findByRestaurantPhoneContaining(String phone);
}