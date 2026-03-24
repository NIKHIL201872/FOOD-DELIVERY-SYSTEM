package com.foodservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customers {
    private Integer customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
}