package com.foodservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCoupons {
    private Integer orderId;
    private Integer couponId;
}