package com.foodservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foodservice.entity.CustomersEntity;

public interface CustomersRepository extends JpaRepository<CustomersEntity, Integer> {
    @Query("SELECT MAX(c.customerId) FROM CustomersEntity c")
    Integer MaxCustomerId();

    List<CustomersEntity> findByCustomerName(String customerName);

    CustomersEntity findByCustomerEmail(String customerEmail);

    CustomersEntity findByCustomerPhone(String customerPhone);
    
    @Query("SELECT c FROM CustomersEntity c WHERE c.customerName = :name AND c.customerEmail = :email")
    List<CustomersEntity> findByNameAndEmail(@Param("name") String name,
                                             @Param("email") String email);

    // 🔹 NAME + PHONE
    @Query("SELECT c FROM CustomersEntity c WHERE c.customerName = :name AND c.customerPhone = :phone")
    List<CustomersEntity> findByNameAndPhone(@Param("name") String name,
                                             @Param("phone") String phone);

    // 🔹 EMAIL + PHONE
    @Query("SELECT c FROM CustomersEntity c WHERE c.customerEmail = :email AND c.customerPhone = :phone")
    CustomersEntity findByEmailAndPhone(@Param("email") String email,
                                        @Param("phone") String phone);
}
