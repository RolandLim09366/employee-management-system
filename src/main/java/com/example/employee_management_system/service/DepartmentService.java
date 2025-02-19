package com.example.employee_management_system.service;

import com.example.employee_management_system.exception.ResourceNotFoundException;
import com.example.employee_management_system.model.Department;
import com.example.employee_management_system.model.Employee;
import com.example.employee_management_system.repository.DepartmentRepo;
import com.example.employee_management_system.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Cacheable(value = "departments")
    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    @Cacheable(value = "department", key = "#id")
    public Department getDepartmentById(Long id) {
        return departmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
    }

    @CacheEvict(value = "departments", allEntries = true)
    public Department createDepartment(Department department) {
        return departmentRepo.save(department);
    }

    @CacheEvict(value = {"departments", "department", "employees"}, allEntries = true)
    public Department updateDepartment(Long id, Department departmentDetails) {
        return departmentRepo.findById(id).map(department -> {
            department.setName(departmentDetails.getName());

            // Update all employees in this department
            List<Employee> employees = employeeRepo.findByDepartmentId(id);
            for (Employee employee : employees) {
                employee.setDepartment(department); // Ensure employees get the updated department name
                employeeRepo.save(employee);
            }

            return departmentRepo.save(department);
        }).orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
    }

    @CacheEvict(value = {"departments", "department"}, allEntries = true, key = "#id")
    public void deleteDepartment(Long id) {
        if (!departmentRepo.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with ID: " + id);
        }
        departmentRepo.deleteById(id);
    }
}
