package com.rathete.employeeapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

//DTO for Employee response
// This class is used to structure the employee data sent back to the client in response to API requests
public record EmployeeResponse(
        Long id,
        String name,
        String email,
        String department,
        BigDecimal salary,
        LocalDate joiningDate
) {}