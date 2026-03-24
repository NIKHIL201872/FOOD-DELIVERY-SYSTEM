package com.foodservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDrivers {
    private Integer driverId;
    private String driverName;
    private String driverPhone;
    private String driverVehicle;
}