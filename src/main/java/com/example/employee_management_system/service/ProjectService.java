package com.example.employee_management_system.service;

import com.example.employee_management_system.exception.ResourceNotFoundException;
import com.example.employee_management_system.model.Employee;
import com.example.employee_management_system.model.Project;
import com.example.employee_management_system.repository.EmployeeRepo;
import com.example.employee_management_system.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepository;

    @Autowired
    private EmployeeRepo employeeRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
        return project;
    }

    public Project createProject(Project project) {
        // Fetch full Employee details from DB before saving
        Set<Employee> employees = fetchEmployeesById(project.getEmployees());

        project.setEmployees(employees);
        return projectRepository.save(project);
    }

    public Project updateProject(Long projectId, Project projectDetails) {
        return projectRepository.findById(projectId).map(project -> {
            project.setName(projectDetails.getName());

            if (projectDetails.getEmployees() != null) {
                // Fetch full Employee details from DB before updating
                Set<Employee> employees = fetchEmployeesById(projectDetails.getEmployees());
                project.setEmployees(employees);
            }

            return projectRepository.save(project);
        }).orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    private Set<Employee> fetchEmployeesById(Set<Employee> employeeSet) {
        return employeeSet.stream()
                .map(emp -> employeeRepository.findById(emp.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + emp.getId())))
                .collect(Collectors.toSet());
    }
}