package com.rathete.employeeapi.service;

import com.rathete.employeeapi.dto.EmployeeResponse;
import com.rathete.employeeapi.dto.EmployeeRequest;

import java.util.List;

// Service interface for Employee operations
// This interface defines the contract for employee-related operations
public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);
    EmployeeResponse getById(Long id);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);
    void deleteEmployee(Long id);
}