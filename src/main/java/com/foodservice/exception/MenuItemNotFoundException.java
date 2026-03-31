package com.foodservice.exception;

public class MenuItemNotFoundException extends ResourceNotFoundException {
    public MenuItemNotFoundException(String message) {
        super(message);
    }
}
