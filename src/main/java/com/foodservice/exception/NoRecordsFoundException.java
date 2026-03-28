package com.foodservice.exception;

public class NoRecordsFoundException extends RuntimeException{
	public NoRecordsFoundException(String message) {
	       super(message);
	   }
}
