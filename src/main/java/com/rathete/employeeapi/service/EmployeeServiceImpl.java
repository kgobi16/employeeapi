package com.rathete.employeeapi.service;

import com.rathete.employeeapi.dto.EmployeeResponse;
import com.rathete.employeeapi.dto.EmployeeRequest;
import com.rathete.employeeapi.enitity.Employee;
import com.rathete.employeeapi.exception.DuplicateEmailException;
import com.rathete.employeeapi.exception.RessourceNotFoundException;
import com.rathete.employeeapi.mapper.EmployeeMapper;
import com.rathete.employeeapi.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

@Transactional(readOnly = true)

// Implementation of EmployeeService interface
// This class provides the business logic for managing employees

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("An employee with the email " + request.email() + " already exists.");
        }
        Employee employee = employeeMapper.toEntity(request);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponse(savedEmployee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    public EmployeeResponse getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Employee with id " + id + " not found."));
        return employeeMapper.toResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Employee with id " + id + " not found."));
        if (!employee.getEmail().equals(request.email()) && employeeRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("An employee with the email " + request.email() + " already exists.");
        }
        employeeMapper.updateEntity(employee, request);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponse(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RessourceNotFoundException("Employee with id " + id + " not found.");
        }
        employeeRepository.deleteById(id);
    }

    private Employee findOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Employee with id " + id + " not found."));
    }

    }
