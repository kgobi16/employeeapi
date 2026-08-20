package com.rathete.employeeapi.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

//DTO for Employee request
// This class is used to structure the employee data received from the client in API requests
public record EmployeeRequest(
        
        //validation for the name field to ensure it is not blank
        @NotBlank(message = "Name is mandatory")
        String name,

        //validation for the email field to ensure it is a valid email and not blank
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is mandatory")
        String email,

        //validation for the department field to ensure it is not blank
        @NotBlank(message = "Department is mandatory")
        String department,

        //validation for the salary field to ensure it is greater than 0
        @NotNull(message = "Salary is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
        BigDecimal salary,

        //validation for the joining date field to ensure it is not in the future
        @PastOrPresent(message = "Joining date cannot be in the future")
        LocalDate joiningDate
) {}