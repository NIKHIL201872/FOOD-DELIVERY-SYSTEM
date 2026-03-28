package com.foodservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItems {
    private Integer itemId;
    private String itemName;
    private String itemDescription;
    private double itemPrice;
    private Integer restaurantId;
}