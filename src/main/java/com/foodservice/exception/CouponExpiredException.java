package com.foodservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class CouponExpiredException extends RuntimeException{
	private String exceptionmsg;
}
