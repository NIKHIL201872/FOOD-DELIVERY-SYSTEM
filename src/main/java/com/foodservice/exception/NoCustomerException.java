package com.foodservice.exception;

public class NoCustomerException extends RuntimeException {
	public NoCustomerException(String msg) {
		super(msg);
	}
}
