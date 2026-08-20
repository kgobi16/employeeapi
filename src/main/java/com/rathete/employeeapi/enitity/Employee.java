package com.rathete.employeeapi.enitity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;



@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

//Entity class for Employee
// This class represents the Employee entity in the database
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String department;

    @Column(nullable = false, scale = 2)
    private BigDecimal salary;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;
}
