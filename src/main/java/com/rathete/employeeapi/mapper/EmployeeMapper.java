package com.rathete.employeeapi.mapper;

import com.rathete.employeeapi.dto.EmployeeResponse;
import com.rathete.employeeapi.dto.EmployeeRequest;
import com.rathete.employeeapi.enitity.Employee;
import org.springframework.stereotype.Component;

@Component

// Mapper class for converting between Employee entity and DTOs
// This class provides methods to convert EmployeeRequest to Employee entity, Employee entity to EmployeeResponse, and update an existing Employee entity with data from EmployeeRequest
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequest request) {
        return Employee.builder()
                .name(request.name())
                .email(request.email())
                .department(request.department())
                .salary(request.salary())
                .joiningDate(request.joiningDate())
                .build();
    }

    public EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary(),
                employee.getJoiningDate()
        );
    }

    public void updateEntity(Employee employee, EmployeeRequest request) {
        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setDepartment(request.department());
        employee.setSalary(request.salary());
        employee.setJoiningDate(request.joiningDate());
    }
}