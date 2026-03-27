package com.foodservice.DAO;

import com.foodservice.entity.DeliveryDriversEntity;
import com.foodservice.exception.ResourceNotFoundException;
import com.foodservice.repository.DeliveryDriversRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryDriversDAO {

    private final DeliveryDriversRepository driverRepo;

    public DeliveryDriversEntity findById(Integer id) {
        return driverRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    }

    public DeliveryDriversEntity save(DeliveryDriversEntity driver) {
        return driverRepo.save(driver);
    }

    public List<DeliveryDriversEntity> findAll() {
        return driverRepo.findAll();
    }

    public void delete(Integer id) {
        driverRepo.deleteById(id);
    }
}