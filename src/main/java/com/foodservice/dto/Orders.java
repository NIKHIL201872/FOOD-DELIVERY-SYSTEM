package com.foodservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    private Integer orderId;
    private LocalDateTime orderDate;
    private String orderStatus;
    private Integer customerId;
    private Integer restaurantId;
    private Integer deliveryDriverId;
}