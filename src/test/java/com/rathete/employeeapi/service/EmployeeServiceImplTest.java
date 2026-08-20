package com.rathete.employeeapi.service;

import com.rathete.employeeapi.dto.EmployeeRequest;
import com.rathete.employeeapi.dto.EmployeeResponse;
import com.rathete.employeeapi.enitity.Employee;
import com.rathete.employeeapi.exception.DuplicateEmailException;
import com.rathete.employeeapi.exception.RessourceNotFoundException;
import com.rathete.employeeapi.mapper.EmployeeMapper;
import com.rathete.employeeapi.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


// Test class for EmployeeServiceImpl
// ExtendWith annotation is used to enable Mockito support in JUnit 5
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock EmployeeRepository repository;
    @Spy  EmployeeMapper mapper = new EmployeeMapper();
    @InjectMocks EmployeeServiceImpl service;

    private EmployeeRequest request;
    private Employee employee;

    @BeforeEach
    void setUp() {
        request = new EmployeeRequest("Thabo Nkosi", "thabo@example.com",
                "Engineering", new BigDecimal("75000.00"), LocalDate.of(2024, 1, 15));

        employee = Employee.builder()
                .id(1L).name("Thabo Nkosi").email("thabo@example.com")
                .department("Engineering").salary(new BigDecimal("75000.00"))
                .joiningDate(LocalDate.of(2024, 1, 15)).build();
    }

    @Test
    void create_savesAndReturnsEmployee() {
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.save(any(Employee.class))).thenReturn(employee);

        EmployeeResponse result = service.createEmployee(request);

        assertThat(result.email()).isEqualTo("thabo@example.com");
        verify(repository).save(any(Employee.class));
    }

    @Test
    void create_throwsWhenEmailDuplicate() {
        when(repository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> service.createEmployee(request))
                .isInstanceOf(DuplicateEmailException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void getById_throwsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void update_allowsKeepingOwnEmail() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.save(any(Employee.class))).thenReturn(employee);

        assertThat(service.updateEmployee(1L, request)).isNotNull();
    }

    @Test
    void delete_throwsWhenMissing() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteEmployee(99L))
                .isInstanceOf(RessourceNotFoundException.class);

        verify(repository, never()).deleteById(any());
    }
}
