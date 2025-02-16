package com.example.employee_management_system.service;

import com.example.employee_management_system.exception.ResourceNotFoundException;
import com.example.employee_management_system.model.Employee;
import com.example.employee_management_system.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    // ✅ Cache all employees - Cache updated when an employee is added/updated/deleted
    @Cacheable(value = "employees")
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    // ✅ Cache individual employee by ID - Improves performance for repeated fetches
    @Cacheable(value = "employee", key = "#id")
    public Employee getEmployeeById(Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }

    // ✅ Add employee and update cache
    @CachePut(value = "employee", key = "#employee.id")
    public Employee saveEmployee(Employee employee) {
        return employeeRepo.save(employee);
    }

    // ✅ Update employee and update cache
    @CachePut(value = "employee", key = "#id")
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        employee.setName(employeeDetails.getName());
        employee.setPosition(employeeDetails.getPosition());
        employee.setDepartment(employeeDetails.getDepartment());

        return employeeRepo.save(employee);
    }

    // ✅ Delete employee and remove from cache
    @CacheEvict(value = "employee", key = "#id")
    public void deleteEmployee(Long id) {
        employeeRepo.deleteById(id);
    }

    // ✅ Clear entire employee cache when needed (e.g., bulk updates)
    @CacheEvict(value = "employees", allEntries = true)
    public void clearEmployeeCache() {
        System.out.println("Clearing all employee cache");
    }
}
