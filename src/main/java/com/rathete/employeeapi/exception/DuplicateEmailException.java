package com.rathete.employeeapi.exception;

// Custom exception for duplicate email scenario
// This exception is thrown when an attempt is made to create or update an employee with an email that already exists in the database
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}