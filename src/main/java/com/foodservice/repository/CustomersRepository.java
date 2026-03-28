package com.foodservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foodservice.entity.CustomersEntity;

public interface CustomersRepository extends JpaRepository<CustomersEntity, Integer> {
    @Query("SELECT MAX(c.customerId) FROM CustomersEntity c")
    Integer MaxCustomerId();

    List<CustomersEntity> findByCustomerName(String customerName);

    CustomersEntity findByCustomerEmail(String customerEmail);

    CustomersEntity findByCustomerPhone(String customerPhone);
}
