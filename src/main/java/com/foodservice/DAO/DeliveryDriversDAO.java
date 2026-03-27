package com.foodservice.DAO;

import com.foodservice.entity.DeliveryDriversEntity;
import com.foodservice.repository.DeliveryDriversRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository

public class DeliveryDriversDAO {
		@Autowired
		DeliveryDriversRepository driverRepo;

    public DeliveryDriversEntity findById(Integer id) {
        return driverRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
    }

    public DeliveryDriversEntity save(DeliveryDriversEntity driver) {
        return driverRepo.save(driver);
    }
}