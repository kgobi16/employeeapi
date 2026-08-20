package com.rathete.employeeapi.exception;

// Custom exception for resource not found scenario
// This exception is thrown when a requested resource (like an employee) is not found in the database
public class RessourceNotFoundException extends RuntimeException {
    public RessourceNotFoundException(String message) {
        super(message);
    }
}