package com.example.employee_management_system.service;

import com.example.employee_management_system.exception.ResourceNotFoundException;
import com.example.employee_management_system.model.Department;
import com.example.employee_management_system.repository.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepo departmentRepo;

    // Create a new department
    public Department createDepartment(Department department) {
        return departmentRepo.save(department);
    }

    // Get all departments
    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    // Get department by ID
    public Department getDepartmentById(Long id) {
        return departmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
    }

    // Update department
    public Department updateDepartment(Long id, Department departmentDetails) {
        return departmentRepo.findById(id).map(department -> {
            department.setName(departmentDetails.getName());
            return departmentRepo.save(department);
        }).orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
    }

    // Delete department
    public void deleteDepartment(Long id) {
        if (!departmentRepo.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with ID: " + id);
        }
        departmentRepo.deleteById(id);
    }
}
