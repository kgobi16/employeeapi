package com.rathete.employeeapi.exception;

import com.rathete.employeeapi.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Global exception handler for the application
@RestControllerAdvice
public class GlobalExceptionHandler {

  
    // Handle resource not found errors
    @ExceptionHandler(RessourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            RessourceNotFoundException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request, null);
    }

    // Handle duplicate email errors
   @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(
            DuplicateEmailException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), request, null);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        });
        return build(HttpStatus.BAD_REQUEST, "Validation Failed", "Validation failed", request, validationErrors);
    }

    // Handle unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred", ex.getMessage(), request, null);
    }

    // Helper method to build the error response
    private ResponseEntity<ErrorResponse> build(
        HttpStatus status, String error, String message, HttpServletRequest request, Map<String, String> validationErrors) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                request.getRequestURI(),
                validationErrors
        );
        
        return ResponseEntity.status(status).body(errorResponse);
    }
}

