package com.raaghul.employeemanagement.dto;

public record EmployeeResponseDTO(
        Long id,
        String name,
        String email,
        String department
) {
}
