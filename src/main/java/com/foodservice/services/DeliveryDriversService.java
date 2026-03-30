package com.foodservice.services;

import com.foodservice.DAO.DeliveryDriversDAO;
import com.foodservice.dto.DeliveryDrivers;
import com.foodservice.entity.DeliveryDriversEntity;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.exception.BadRequestException;
import com.foodservice.exception.ResourceConflictException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DeliveryDriversService {

    private final DeliveryDriversDAO driverDAO;

    // CREATE DRIVER
    public String createDriver(DeliveryDrivers dto) {

        
        if (dto == null) {
            throw new BadRequestException("Driver cannot be null");
        }


        if (dto.getDriverId() == null) {
            throw new BadRequestException("Driver ID must be provided");
        }

        
        if (driverDAO.existsById(dto.getDriverId())) {
            throw new ResourceConflictException("Driver ID already exists");
        }

        
        driverDAO.save(convertToEntity(dto));

        return "Driver inserted successfully with ID: " + dto.getDriverId();
    }    

    private DeliveryDriversEntity convertToEntity(DeliveryDrivers dto) {
    	DeliveryDriversEntity entity = new DeliveryDriversEntity();

        
    	entity.setDriverId(dto.getDriverId());
        entity.setDriverName(dto.getDriverName());
        entity.setDriverPhone(dto.getDriverPhone());
        entity.setDriverVehicle(dto.getDriverVehicle());

        return entity;
	}

	// GET ONE DRIVER
    public DeliveryDrivers getDriver(Integer id) {
        DeliveryDriversEntity driver = driverDAO.findById(id);
        return mapToDTO(driver);
    }

    // GET ALL DRIVERS
    public List<DeliveryDrivers> getAllDrivers() {
        return driverDAO.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // UPDATE DRIVER
    public DeliveryDrivers updateDriver(Integer id, DeliveryDrivers dto) {

        DeliveryDriversEntity driver = driverDAO.findById(id);

        driver.setDriverName(dto.getDriverName());
        driver.setDriverPhone(dto.getDriverPhone());
        driver.setDriverVehicle(dto.getDriverVehicle());

        return mapToDTO(driverDAO.save(driver));
    }

    // DELETE DRIVER
    public void deleteDriver(Integer id) {
        driverDAO.delete(id);
    }

    // MAPPER
    private DeliveryDrivers mapToDTO(DeliveryDriversEntity driver) {
        return new DeliveryDrivers(
                driver.getDriverId(),
                driver.getDriverName(),
                driver.getDriverPhone(),
                driver.getDriverVehicle()
        );
    }
 //  // 🔥 BUSINESS LOGIC

    public void assignDriverToOrder(Integer driverId, Integer orderId) {
    	driverDAO.assignDriverToOrder(driverId, orderId);
    }

    public List<OrdersEntity> getOrdersByDriver(Integer driverId) {
        return driverDAO.getOrdersByDriver(driverId);
    }

    public void completeDelivery(Integer driverId, Integer orderId) {
    	driverDAO.completeDelivery(driverId, orderId);
    }
}