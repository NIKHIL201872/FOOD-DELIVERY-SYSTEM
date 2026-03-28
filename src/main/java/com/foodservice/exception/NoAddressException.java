package com.foodservice.exception;


public class NoAddressException extends RuntimeException{
	public NoAddressException(String msg) {
		super(msg);
	}
}
