package com.foodservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    private Integer ratingId;
    private Integer orderId;
    private Integer restaurantId;
    private Integer rating;
    private String review;
}