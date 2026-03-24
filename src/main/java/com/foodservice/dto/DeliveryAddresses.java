package com.foodservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddresses {
    private Integer addressId;
    private Integer customerId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
}