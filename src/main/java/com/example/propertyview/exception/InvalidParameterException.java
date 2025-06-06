package com.example.propertyview.exception;

import org.springframework.http.HttpStatus;

public class InvalidParameterException extends PropertyViewServiceException {

    public InvalidParameterException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
