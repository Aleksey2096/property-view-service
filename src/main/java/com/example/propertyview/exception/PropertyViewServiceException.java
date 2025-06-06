package com.example.propertyview.exception;

import org.springframework.http.HttpStatus;

public abstract class PropertyViewServiceException extends RuntimeException {
    PropertyViewServiceException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();
}
