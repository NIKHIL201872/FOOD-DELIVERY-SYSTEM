package com.foodservice.exception;

public class OrderItemNotFoundException extends ResourceNotFoundException {
    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
