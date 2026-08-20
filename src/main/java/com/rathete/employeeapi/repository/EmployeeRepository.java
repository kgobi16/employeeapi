package com.rathete.employeeapi.repository;

import com.rathete.employeeapi.enitity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

//Repository interface for Employee entity
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email); // Check if an employee with the given email already exists


    // when updating an employee, we need to check if the email already exists for another employee (excluding the current employee being updated)
    boolean existsByEmailAndIdNot(String email, Long id); // Check if an employee with the given email exists, excluding the employee with the given id


}