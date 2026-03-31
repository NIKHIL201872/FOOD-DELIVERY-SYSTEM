package com.foodservice.dto;

import lombok.*;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItems {
    private Integer orderItemId;
    private Integer orderId;
    private Integer itemId;
    private Integer quantity;

    // ── ENRICHED FIELDS (only populated in getOrderItemDetails) ──
    private String itemName;
    private BigDecimal itemPrice;
    private BigDecimal subtotal;
    private String itemDescription;
    private Integer restaurantid;
}