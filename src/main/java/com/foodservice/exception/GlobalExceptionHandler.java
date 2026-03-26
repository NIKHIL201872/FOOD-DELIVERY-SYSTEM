package com.foodservice.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(NoCustomerException.class)
	public void NoCustomerExceptionHandler(NoCustomerException ex) {
		String msg = ex.getExceptionMsg();
		System.out.println(msg);
	}
	
	@ExceptionHandler(NoAddressException.class)
	public void NoAddressExceptionHandler(NoAddressException ex) {
		String msg = ex.getExceptionMsg();
		System.out.println(msg);
	}
}
