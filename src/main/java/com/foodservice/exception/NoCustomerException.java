package com.foodservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NoCustomerException extends RuntimeException {
	private String exceptionMsg;
}
