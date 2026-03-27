package com.foodservice.exception;

<<<<<<< HEAD

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
=======
>>>>>>> 6ea5a46cd5fe85cbf11ff0d53ebc19a62ce4d082
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}