package com.foodservice.exception;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Common method to build response
    private ApiErrorResponse buildResponse(Exception ex, HttpStatus status, HttpServletRequest request) {
        return new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // 400 - Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(
            BadRequestException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.BAD_REQUEST, request),
                HttpStatus.BAD_REQUEST
        );
    }

    // Coupon Expired
    @ExceptionHandler(CouponExpiredException.class)
    public ResponseEntity<ApiErrorResponse> handleCouponExpired(
            CouponExpiredException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.BAD_REQUEST, request),
                HttpStatus.BAD_REQUEST
        );
    }

    // Coupon Not Found
    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCouponNotFound(
            CouponNotFoundException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.NOT_FOUND, request),
                HttpStatus.NOT_FOUND
        );
    }

    //  Duplicate Coupon
    @ExceptionHandler(DuplicateCouponException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateCoupon(
            DuplicateCouponException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.CONFLICT, request),
                HttpStatus.CONFLICT
        );
    }

    //  No Address
    @ExceptionHandler(NoAddressException.class)
    public ResponseEntity<ApiErrorResponse> handleNoAddress(
            NoAddressException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.NOT_FOUND, request),
                HttpStatus.NOT_FOUND
        );
    }

    //  No Customer
    @ExceptionHandler(NoCustomerException.class)
    public ResponseEntity<ApiErrorResponse> handleNoCustomer(
            NoCustomerException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.NOT_FOUND, request),
                HttpStatus.NOT_FOUND
        );
    }

    //  No Records Found
    @ExceptionHandler(NoRecordsFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoRecords(
            NoRecordsFoundException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.NOT_FOUND, request),
                HttpStatus.NOT_FOUND
        );
    }

    //  Resource Conflict
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(
            ResourceConflictException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.CONFLICT, request),
                HttpStatus.CONFLICT
        );
    }

    //  MenuItem Conflict
    @ExceptionHandler(MenuItemConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleMenuItemConflict(
            MenuItemConflictException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.CONFLICT, request),
                HttpStatus.CONFLICT
        );
    }

    //  OrderItem Conflict
    @ExceptionHandler(OrderItemConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleOrderItemConflict(
            OrderItemConflictException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.CONFLICT, request),
                HttpStatus.CONFLICT
        );
    }

    //  Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.NOT_FOUND, request),
                HttpStatus.NOT_FOUND
        );
    }

    //  MenuItem Not Found
    @ExceptionHandler(MenuItemNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleMenuItemNotFound(
            MenuItemNotFoundException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.NOT_FOUND, request),
                HttpStatus.NOT_FOUND
        );
    }

    //  OrderItem Not Found
    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleOrderItemNotFound(
            OrderItemNotFoundException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.NOT_FOUND, request),
                HttpStatus.NOT_FOUND
        );
    }

    
    
    //testing purposes
    @ExceptionHandler(DuplicateCustomerException.class)
    public String handleDuplicate(DuplicateCustomerException ex) {
        return ex.getMessage();
    }
    
    
    //  Catch ALL (Fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(
            Exception ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                buildResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

