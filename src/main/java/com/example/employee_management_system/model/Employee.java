package com.example.employee_management_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Entity
@Table(name = "employee")
@Getter
@Setter
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;  // ✅ Add serialVersionUID

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, message = "Name must be at least 2 characters")
    private String name;

    @NotBlank(message = "Position is required")
    private String position;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @NotNull(message = "Department is required")
    private Department department;}