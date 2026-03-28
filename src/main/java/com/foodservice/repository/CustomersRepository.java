package com.foodservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodservice.entity.CustomersEntity;

public interface CustomersRepository extends JpaRepository<CustomersEntity, Integer>{
 

}
