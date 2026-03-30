package com.foodservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingOnlyDTO {
	private Integer ratingId;
    private Integer rating;
    private String review;
    private Integer restaurantId;
    private Integer orderId;
}