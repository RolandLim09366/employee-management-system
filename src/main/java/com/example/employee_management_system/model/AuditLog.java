package com.example.employee_management_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action; // e.g., "CREATE", "UPDATE", "DELETE"
    private String entityName;
    private Long entityId;
    private LocalDateTime timestamp;
    private String username; // User performing the action
}
