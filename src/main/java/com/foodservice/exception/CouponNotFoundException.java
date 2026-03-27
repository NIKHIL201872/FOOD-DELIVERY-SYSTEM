package com.foodservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class CouponNotFoundException extends RuntimeException{
	private String exceptionmsg;
}