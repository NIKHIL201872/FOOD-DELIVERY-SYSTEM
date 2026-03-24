package com.foodservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "deliverydrivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDriversEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer driverId;

    private String driverName;
    private String driverPhone;
    private String driverVehicle;

    @OneToMany(mappedBy = "deliveryDriver")
    private List<OrdersEntity> orders;
}