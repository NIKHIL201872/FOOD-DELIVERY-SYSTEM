package com.foodservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderSummary {
    private Integer customerId;
    private Long totalOrders;
    private Integer topRestaurantId;  // most ordered from
}