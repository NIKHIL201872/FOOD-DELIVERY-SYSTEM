package com.foodservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupons {
    private Integer couponId;
    private String couponCode;
    private BigDecimal discountAmount;
    private LocalDate expiryDate;
}