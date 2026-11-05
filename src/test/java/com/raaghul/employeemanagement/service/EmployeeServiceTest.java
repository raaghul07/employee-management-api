package com.raaghul.employeemanagement.service;

import com.raaghul.employeemanagement.model.Employee;
import com.raaghul.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    @Test
    void shouldReturnAllEmployees() {
        Employee employee = new Employee(
                "John Smith",
                "john.smith@example.com",
                "Engineering"
        );

        when(repository.findAll()).thenReturn(List.of(employee));

        List<Employee> result = service.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("John Smith", result.get(0).getName());

        verify(repository).findAll();
    }

    @Test
    void shouldCreateEmployee() {
        Employee employee = new Employee(
                "Jane Smith", "jane@example.com", "Finance");

        when(repository.findByEmail("jane@example.com"))
                .thenReturn(java.util.Optional.empty());
        when(repository.save(employee)).thenReturn(employee);

        Employee result = service.createEmployee(employee);

        assertEquals("Jane Smith", result.getName());
        verify(repository).save(employee);
    }

    @Test
    void shouldRejectDuplicateEmail() {
        Employee employee = new Employee(
                "Jane Smith", "jane@example.com", "Finance");

        when(repository.findByEmail("jane@example.com"))
                .thenReturn(java.util.Optional.of(employee));

        org.springframework.web.server.ResponseStatusException exception =
                assertThrows(
                        org.springframework.web.server.ResponseStatusException.class,
                        () -> service.createEmployee(employee));

        assertEquals(409, exception.getStatusCode().value());
        verify(repository, never()).save(any(Employee.class));
    }

    @Test
    void shouldReturn404ForMissingEmployee() {
        when(repository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        org.springframework.web.server.ResponseStatusException exception =
                assertThrows(
                        org.springframework.web.server.ResponseStatusException.class,
                        () -> service.getEmployeeById(999L));

        assertEquals(404, exception.getStatusCode().value());
    }

}
