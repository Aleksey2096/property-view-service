package com.example.propertyview.exception.handler;

import com.example.propertyview.exception.PropertyViewServiceException;
import com.example.propertyview.exception.ResponseExceptionBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseExceptionBody> handleException(Exception e, HttpServletRequest request) {
        log.error("Exception {} occurred with message: '{}'. Request URI: {}",
                e.getClass().getName(), e.getMessage(), request.getRequestURI(), e);

        ResponseExceptionBody exceptionBody = new ResponseExceptionBody(
                BAD_REQUEST.value(),
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler(PropertyViewServiceException.class)
    public ResponseEntity<ResponseExceptionBody> handleBankCardServiceException(PropertyViewServiceException e,
                                                                                HttpServletRequest request) {
        log.error("Exception {} occurred with message: '{}'. Request URI: {}.",
                e.getClass().getName(), e.getMessage(), request.getRequestURI(), e);

        ResponseExceptionBody exceptionBody = new ResponseExceptionBody(
                e.getHttpStatus().value(),
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(e.getHttpStatus()).body(exceptionBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseExceptionBody> handleValidationException(MethodArgumentNotValidException e,
                                                                           HttpServletRequest request) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.error("{} is thrown: Issues - {}; ", e.getClass().getName(), String.join(", ", errors), e);

        ResponseExceptionBody exceptionBody = new ResponseExceptionBody(
                HttpStatus.BAD_REQUEST.value(),
                errors,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }
}
