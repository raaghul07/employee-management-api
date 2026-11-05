
package com.raaghul.employeemanagement.service;

import com.raaghul.employeemanagement.model.Employee;
import com.raaghul.employeemanagement.repository.EmployeeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    @Test
    void shouldReturnPaginatedEmployees() {
        Employee employee = new Employee(
                "John Smith",
                "john.smith@example.com",
                "Engineering"
        );

        PageRequest pageable = PageRequest.of(
                0, 10, Sort.by("name").ascending());

        when(repository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(employee), pageable, 1));

        Page<Employee> result =
                service.getAllEmployees(0, 10, "name", "asc");

        assertEquals(1, result.getTotalElements());
        assertEquals("John Smith", result.getContent().get(0).getName());

        verify(repository).findAll(pageable);
    }

    @Test
    void shouldSupportDescendingSort() {
        PageRequest pageable = PageRequest.of(
                0, 5, Sort.by("id").descending());

        when(repository.findAll(pageable))
                .thenReturn(Page.empty(pageable));

        service.getAllEmployees(0, 5, "id", "desc");

        verify(repository).findAll(pageable);
    }

    @Test
    void shouldRejectInvalidPageSize() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.getAllEmployees(0, 0, "id", "asc")
        );

        assertEquals(400, exception.getStatusCode().value());

        verifyNoInteractions(repository);
    }

    @Test
    void shouldCreateEmployee() {
        Employee employee = new Employee(
                "Jane Smith", "jane@example.com", "Finance");

        when(repository.findByEmail("jane@example.com"))
                .thenReturn(Optional.empty());

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
                .thenReturn(Optional.of(employee));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.createEmployee(employee)
        );

        assertEquals(409, exception.getStatusCode().value());

        verify(repository, never()).save(any(Employee.class));
    }

    @Test
    void shouldReturn404ForMissingEmployee() {
        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.getEmployeeById(999L)
        );

        assertEquals(404, exception.getStatusCode().value());
    }
}
