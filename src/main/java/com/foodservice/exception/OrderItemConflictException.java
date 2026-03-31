package com.foodservice.exception;

public class OrderItemConflictException extends ResourceConflictException {
    public OrderItemConflictException(String message) {
        super(message);
    }
}
