
package com.raaghul.employeemanagement.service;

import com.raaghul.employeemanagement.model.Employee;
import com.raaghul.employeemanagement.repository.EmployeeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Page<Employee> getAllEmployees(
            int page,
            int size,
            String sortBy,
            String direction,
            String name,
            String department) {

        // Validate pagination
        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page must be >= 0 and size must be between 1 and 100");
        }

        // Validate sorting field
        if (!Set.of("id", "name", "email", "department")
                .contains(sortBy)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid sort field");
        }

        // Validate sorting direction
        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid sort direction");
        }

        // Configure sorting
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // Configure pagination
        Pageable pageable = PageRequest.of(page, size, sort);

        // Normalize optional search parameters
        String searchName = name == null ? "" : name.trim();
        String searchDepartment = department == null
                ? ""
                : department.trim();

        boolean hasName = !searchName.isEmpty();
        boolean hasDepartment = !searchDepartment.isEmpty();

        // Search by both name and department
        if (hasName && hasDepartment) {
            return repository
                    .findByNameContainingIgnoreCaseAndDepartmentContainingIgnoreCase(
                            searchName,
                            searchDepartment,
                            pageable);
        }

        // Search by name only
        if (hasName) {
            return repository.findByNameContainingIgnoreCase(
                    searchName,
                    pageable);
        }

        // Filter by department only
        if (hasDepartment) {
            return repository.findByDepartmentContainingIgnoreCase(
                    searchDepartment,
                    pageable);
        }

        // No filters: return all employees
        return repository.findAll(pageable);
    }

    public Employee getEmployeeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Employee not found"));
    }

    public Employee createEmployee(Employee employee) {
        employee.setEmail(
                employee.getEmail().trim().toLowerCase());

        if (repository.findByEmail(employee.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists");
        }

        return repository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updated) {
        Employee existing = getEmployeeById(id);

        existing.setName(updated.getName());

        String newEmail = updated.getEmail()
                .trim()
                .toLowerCase();

        repository.findByEmail(newEmail).ifPresent(other -> {
            if (!other.getId().equals(id)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Email already exists");
            }
        });

        existing.setEmail(newEmail);
        existing.setDepartment(updated.getDepartment());

        return repository.save(existing);
    }

    public void deleteEmployee(Long id) {
        Employee existing = getEmployeeById(id);
        repository.delete(existing);
    }
}
