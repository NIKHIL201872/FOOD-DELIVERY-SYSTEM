package com.foodservice.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    private Integer orderId;

    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Restaurant ID is required")
    private Integer restaurantId;

    private Integer deliveryDriverId;

    private String orderStatus;
    private LocalDateTime orderDate;
}