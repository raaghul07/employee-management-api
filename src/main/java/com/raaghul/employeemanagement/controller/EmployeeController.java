
package com.raaghul.employeemanagement.controller;

import com.raaghul.employeemanagement.dto.EmployeeRequestDTO;
import com.raaghul.employeemanagement.dto.EmployeeResponseDTO;
import com.raaghul.employeemanagement.model.Employee;
import com.raaghul.employeemanagement.service.EmployeeService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private EmployeeResponseDTO toResponse(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment()
        );
    }

    // Get employees with pagination, sorting and optional filters
    @GetMapping
    public Page<EmployeeResponseDTO> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department) {

        return employeeService
                .getAllEmployees(
                        page,
                        size,
                        sortBy,
                        direction,
                        name,
                        department
                )
                .map(this::toResponse);
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public EmployeeResponseDTO getEmployeeById(
            @PathVariable Long id) {

        return toResponse(employeeService.getEmployeeById(id));
    }

    // Create employee
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Valid @RequestBody EmployeeRequestDTO request) {

        Employee employee = new Employee(
                request.name(),
                request.email(),
                request.department()
        );

        Employee saved = employeeService.createEmployee(employee);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    // Update employee
    @PutMapping("/{id}")
    public EmployeeResponseDTO updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDTO request) {

        Employee employee = new Employee(
                request.name(),
                request.email(),
                request.department()
        );

        return toResponse(
                employeeService.updateEmployee(id, employee)
        );
    }

    // Delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
}
