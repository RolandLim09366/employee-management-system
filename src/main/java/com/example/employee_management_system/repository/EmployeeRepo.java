package com.example.employee_management_system.repository;

import com.example.employee_management_system.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    // Custom method to find employees by department ID
    List<Employee> findByDepartmentId(Long departmentId);
}

