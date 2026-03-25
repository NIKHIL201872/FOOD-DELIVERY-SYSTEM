package com.foodservice.DAO;

import com.foodservice.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdersDAO extends JpaRepository<OrdersEntity, Integer> {
    List<OrdersEntity> findByCustomerId(Integer customerId);
    List<OrdersEntity> findByDeliveryDriverId(Integer driverId);
}