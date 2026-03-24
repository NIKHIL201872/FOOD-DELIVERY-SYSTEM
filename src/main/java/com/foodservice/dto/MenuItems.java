package com.foodservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItems {
    private Integer itemId;
    private String itemName;
    private String itemDescription;
    private BigDecimal itemPrice;
    private Integer restaurantId;
}