package com.foodservice.services;

import com.foodservice.DAO.DeliveryDriversDAO;
import com.foodservice.dto.DeliveryDrivers;
import com.foodservice.entity.DeliveryDriversEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryDriversService {

    private final DeliveryDriversDAO driverDAO;

    // CREATE DRIVER
    public DeliveryDrivers createDriver(DeliveryDrivers dto) {

        DeliveryDriversEntity driver = new DeliveryDriversEntity();
        driver.setDriverName(dto.getDriverName());
        driver.setDriverPhone(dto.getDriverPhone());
        driver.setDriverVehicle(dto.getDriverVehicle());

        DeliveryDriversEntity saved = driverDAO.save(driver);

        return mapToDTO(saved);
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
}