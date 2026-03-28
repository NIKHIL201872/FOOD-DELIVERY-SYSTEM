package com.foodservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodservice.entity.DeliveryAddressesEntity;

public interface DeliveryAddressesRepository extends JpaRepository<DeliveryAddressesEntity,Integer>{
	List<DeliveryAddressesEntity> findByCustomerCustomerId(int customerId);
	
	List<DeliveryAddressesEntity> findByCity(String city);

    List<DeliveryAddressesEntity> findByState(String state);

    List<DeliveryAddressesEntity> findByPostalCode(String postalCode);
}
