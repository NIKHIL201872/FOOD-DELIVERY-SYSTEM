package com.foodservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {
    private Integer orderItemId;
    private Integer orderId;
    private Integer itemId;
    private Integer quantity;
}