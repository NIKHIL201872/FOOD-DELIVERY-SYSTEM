package com.foodservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemWithRating {
    private Integer itemId;
    private String itemName;
    private String itemDescription;
    private double itemPrice;
    private Integer restaurantId;
    private Integer rating;
}
