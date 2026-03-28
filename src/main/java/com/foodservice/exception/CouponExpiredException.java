package com.foodservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CouponExpiredException extends RuntimeException {
	public CouponExpiredException(String message) {
		 super(message);
	}
}

