
package com.raaghul.employeemanagement.repository;

import com.raaghul.employeemanagement.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    Page<Employee> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Page<Employee> findByDepartmentContainingIgnoreCase(
            String department,
            Pageable pageable
    );

    Page<Employee> findByNameContainingIgnoreCaseAndDepartmentContainingIgnoreCase(
            String name,
            String department,
            Pageable pageable
    );
}
