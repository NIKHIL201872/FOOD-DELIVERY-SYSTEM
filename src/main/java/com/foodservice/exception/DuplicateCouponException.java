package com.foodservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class DuplicateCouponException  extends RuntimeException{
	private String exceptionmsg;
}
