package com.rathete.employeeapi.controller;

import com.rathete.employeeapi.dto.EmployeeRequest;
import com.rathete.employeeapi.dto.EmployeeResponse;
import com.rathete.employeeapi.exception.DuplicateEmailException;
import com.rathete.employeeapi.exception.RessourceNotFoundException;
import com.rathete.employeeapi.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Test class for EmployeeController
//WebMvcTest annotation is used to test the EmployeeController in isolation

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean EmployeeService service;

    private final EmployeeResponse response = new EmployeeResponse(
            1L, "Thabo Nkosi", "thabo@example.com", "Engineering",
            new BigDecimal("75000.00"), LocalDate.of(2024, 1, 15));

    @Test
    void create_returns201() throws Exception {
        var request = new EmployeeRequest("Thabo Nkosi", "thabo@example.com",
                "Engineering", new BigDecimal("75000.00"), LocalDate.of(2024, 1, 15));

        when(service.createEmployee(any())).thenReturn(response);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_returns400OnBlankName() throws Exception {
        var invalid = new EmployeeRequest("", "not-an-email", "Engineering",
                new BigDecimal("-5"), LocalDate.of(2024, 1, 15));

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validationErrors.name").exists())
            .andExpect(jsonPath("$.validationErrors.email").exists())
            .andExpect(jsonPath("$.validationErrors.salary").exists());
    }

    @Test
    void getById_returns404WhenMissing() throws Exception {
        when(service.getById(99L))
            .thenThrow(new RessourceNotFoundException("Employee not found with id: 99"));

        mockMvc.perform(get("/api/employees/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void create_returns409OnDuplicateEmail() throws Exception {
        var request = new EmployeeRequest("Thabo Nkosi", "thabo@example.com",
                "Engineering", new BigDecimal("75000.00"), LocalDate.of(2024, 1, 15));

        when(service.createEmployee(any())).thenThrow(new DuplicateEmailException("Email already in use"));

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/employees/1"))
            .andExpect(status().isNoContent());
    }
}
