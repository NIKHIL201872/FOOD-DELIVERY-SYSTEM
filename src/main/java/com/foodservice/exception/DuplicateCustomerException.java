package com.foodservice.exception;

public class DuplicateCustomerException extends RuntimeException{
	public DuplicateCustomerException(String msg) {
		super(msg);
	}
}
