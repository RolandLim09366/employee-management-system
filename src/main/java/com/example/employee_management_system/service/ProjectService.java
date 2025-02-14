package com.example.employee_management_system.service;

import com.example.employee_management_system.exception.ResourceNotFoundException;
import com.example.employee_management_system.model.Project;
import com.example.employee_management_system.repository.EmployeeRepo;
import com.example.employee_management_system.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    public Project updateProject(Long projectId, Project projectDetails) {
        return projectRepository.findById(projectId).map(project -> {
            project.setName(projectDetails.getName());

            if (projectDetails.getEmployees() != null) {
                project.setEmployees(projectDetails.getEmployees());
            }

            return projectRepository.save(project);
        }).orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));
    }
}


