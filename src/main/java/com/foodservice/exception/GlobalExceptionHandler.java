package com.foodservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateCouponException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public String duplicateCouponHandler(DuplicateCouponException ex) {
	    return ex.getExceptionmsg(); 
	}

    @ExceptionHandler(CouponNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String couponNotFoundHandler(CouponNotFoundException ex) {
        return ex.getExceptionmsg();
    }

    @ExceptionHandler(CouponExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String couponExpiredHandler(CouponExpiredException ex) {
        return ex.getExceptionmsg();
    }
    
    @ExceptionHandler(NoRecordsFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String NoRecordsFoundHandler(NoRecordsFoundException ex) {
        return ex.getExceptionmsg();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String generalHandler(Exception ex) {
        return "Something went wrong: " + ex.getMessage();
    }
}