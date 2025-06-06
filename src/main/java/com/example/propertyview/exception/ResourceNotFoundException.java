package com.example.propertyview.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends PropertyViewServiceException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
