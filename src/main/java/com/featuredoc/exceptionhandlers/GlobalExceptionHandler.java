package com.featuredoc.exceptionhandlers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        return buildErrorResponse(ex, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        return buildErrorResponse(ex, ex.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(DataIntegrityViolationException ex) {
        return buildErrorResponse(ex, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return buildErrorResponse(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return buildErrorResponse(e, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable rootCause = ex.getRootCause();
        String message;

        if (rootCause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) rootCause;
            String fieldName = invalidFormatException.getPath().get(0).getFieldName();
            Class<?> targetType = invalidFormatException.getTargetType();
            message = "Invalid request body: " + fieldName + " must be a " + targetType.getSimpleName();
        } else {
            message = "Invalid request body: Please check the input format and types.";
        }

        return buildErrorResponse(ex, message, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        Class<?> requiredType = ex.getRequiredType();
        String message = "Invalid input: "+ paramName + " must be a positive integer";
        return buildErrorResponse(ex, message, HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<Map<String, String>> buildErrorResponse(Exception ex, String message, HttpStatus status) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message != null ? message : ex.getMessage());
        response.put("status", String.valueOf(status.value()));
        response.put("trace", ex.getStackTrace().toString());
        response.put("error", status.getReasonPhrase());
        response.put("timestamp", LocalDateTime.now().toString());
        return new ResponseEntity<>(response, status);
    }
}
