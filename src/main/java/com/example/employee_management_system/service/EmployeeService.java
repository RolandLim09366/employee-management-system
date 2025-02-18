package com.example.employee_management_system.service;

import com.example.employee_management_system.exception.ResourceNotFoundException;
import com.example.employee_management_system.model.Department;
import com.example.employee_management_system.model.Employee;
import com.example.employee_management_system.repository.DepartmentRepo;
import com.example.employee_management_system.repository.EmployeeRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private DepartmentRepo departmentRepo; // Inject DepartmentRepo

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(value = "employees")
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    @Cacheable(value = "employee", key = "#id")
    public Employee getEmployeeById(Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }

    @CacheEvict(value = "employees", allEntries = true)  // Evict the entire employees list cache after saving
    @CachePut(value = "employee", key = "#employee.id")
    public Employee createEmployee(Employee employee) {
        // Ensure department exists and fetch full entity
        Department department = departmentRepo.findById(employee.getDepartment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + employee.getDepartment().getId()));

        employee.setDepartment(department); // Set the full department entity

        return employeeRepo.save(employee);
    }

    @CacheEvict(value = "employees", allEntries = true)  // Evict the entire employees list cache after saving
    @CachePut(value = "employee", key = "#id")
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        employee.setName(employeeDetails.getName());
        employee.setPosition(employeeDetails.getPosition());

        Department department = departmentRepo.findById(employeeDetails.getDepartment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + employeeDetails.getDepartment().getId()));

        employee.setDepartment(department);

        return employeeRepo.save(employee);
    }

    @CacheEvict(value = {"employees", "employee"}, allEntries = true, key = "#id") // Evict both list and individual employee cache
    public void deleteEmployee(Long id) {
        if (!employeeRepo.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        employeeRepo.deleteById(id);
    }
}