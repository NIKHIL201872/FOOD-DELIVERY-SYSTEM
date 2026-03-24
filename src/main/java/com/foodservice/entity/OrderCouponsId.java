package com.foodservice.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCouponsId implements Serializable {
    private Integer order;
    private Integer coupon;
}