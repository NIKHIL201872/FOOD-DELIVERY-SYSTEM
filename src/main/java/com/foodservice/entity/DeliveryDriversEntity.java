package com.foodservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @JsonManagedReference 
    private List<OrdersEntity> orders;
}