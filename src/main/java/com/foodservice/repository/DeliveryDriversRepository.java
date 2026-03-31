package com.foodservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodservice.dto.DeliveryDrivers;
import com.foodservice.entity.DeliveryDriversEntity;
import com.foodservice.entity.OrdersEntity;

public interface DeliveryDriversRepository extends JpaRepository<DeliveryDriversEntity, Integer>{

}
