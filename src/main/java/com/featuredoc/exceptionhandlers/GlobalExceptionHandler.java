package com.featuredoc.exceptionhandlers;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        errors.put("status", "400"); //TODO: @Katlego - we need a better way to build these errors
        errors.put("trace", ex.getStackTrace().toString()); //TODO: @Katlego - we need to find a way to print the actual trace
        errors.put("error", "Bad Request");
        errors.put("timestamp", LocalDateTime.now().toString());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("message", ex.getMessage());
        errors.put("status", "400"); //TODO: @Katlego - we need a better way to build these errors
        errors.put("trace", ex.getStackTrace().toString());
        errors.put("error", "Bad Request");
        errors.put("timestamp", LocalDateTime.now().toString());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
