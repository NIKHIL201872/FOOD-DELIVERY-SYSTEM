package com.foodservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NoAddressException extends RuntimeException{
	private String exceptionMsg;
}
