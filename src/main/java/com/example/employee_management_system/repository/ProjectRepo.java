package com.example.employee_management_system.repository;

import com.example.employee_management_system.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo extends JpaRepository<Project, Long> {
}
