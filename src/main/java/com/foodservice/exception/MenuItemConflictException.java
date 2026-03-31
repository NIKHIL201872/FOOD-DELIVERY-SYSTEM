package com.foodservice.exception;

public class MenuItemConflictException extends ResourceConflictException {
    public MenuItemConflictException(String message) {
        super(message);
    }
}
