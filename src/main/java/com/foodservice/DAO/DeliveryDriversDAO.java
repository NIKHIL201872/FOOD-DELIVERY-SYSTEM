package com.foodservice.DAO;

import com.foodservice.entity.DeliveryDriversEntity;
import com.foodservice.entity.OrdersEntity;
import com.foodservice.exception.ResourceNotFoundException;
import com.foodservice.repository.DeliveryDriversRepository;
import com.foodservice.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryDriversDAO {

    private final DeliveryDriversRepository driverRepo;
    private final OrdersRepository ordersRepo;

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

 // 🔥 Assign Driver to Order
    public void assignDriverToOrder(Integer driverId, Integer orderId) {

        DeliveryDriversEntity driver = findById(driverId);

        OrdersEntity order = ordersRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setDeliveryDriver(driver); // ✅ JPA relation
        order.setOrderStatus("OUT_FOR_DELIVERY");

        ordersRepo.save(order);
    }

    // 🔥 Get Orders by Driver
    public List<OrdersEntity> getOrdersByDriver(Integer driverId) {
        DeliveryDriversEntity driver = findById(driverId);
        return ordersRepo.findByDeliveryDriver(driver); // ✅ better than lazy
    }

    // 🔥 Complete Delivery
    public void completeDelivery(Integer driverId, Integer orderId) {

        OrdersEntity order = ordersRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getDeliveryDriver() == null ||
            !order.getDeliveryDriver().getDriverId().equals(driverId)) {
            throw new RuntimeException("Driver not assigned to this order");
        }

        order.setOrderStatus("DELIVERED");

        ordersRepo.save(order);
    }

	public boolean existsById(Integer driverId) {
		return driverRepo.existsById(driverId);	}

    
}