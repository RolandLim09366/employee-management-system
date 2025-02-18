package com.example.employee_management_system.controller;

import com.example.employee_management_system.model.Department;
import com.example.employee_management_system.model.Employee;
import com.example.employee_management_system.repository.DepartmentRepo;
import com.example.employee_management_system.repository.EmployeeRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        Department department = new Department();
        department.setName("IT");
        department = departmentRepo.save(department); // Persist the department first

        employee = new Employee();
        employee.setName("Alice");
        employee.setPosition("Manager");
        employee.setDepartment(department);

        // Save an employee for testing
        employee = employeeRepo.save(employee);
    }

    @BeforeEach
    void clearCache() {
        if (cacheManager.getCache("employees") != null) {
            cacheManager.getCache("employees").clear();
        }
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testGetAllEmployees() throws Exception {
        long initialCount = employeeRepo.count();

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(initialCount));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testGetEmployeeById_Success() throws Exception {
        mockMvc.perform(get("/employees/" + employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testGetEmployeeById_NotFound() throws Exception {
        mockMvc.perform(get("/employees/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testCreateEmployee_Success() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setName("Bob");
        newEmployee.setPosition("Engineer");
        newEmployee.setDepartment(employee.getDepartment());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.position").value("Engineer"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testCreateEmployee_InvalidData() throws Exception {
        Employee invalidEmployee = new Employee(); // Missing required fields

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmployee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testUpdateEmployee_Success() throws Exception {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("Updated Alice");
        updatedEmployee.setPosition("Senior Manager");
        updatedEmployee.setDepartment(employee.getDepartment());

        mockMvc.perform(put("/employees/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Alice"))
                .andExpect(jsonPath("$.position").value("Senior Manager"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testUpdateEmployee_InvalidData() throws Exception {
        Employee invalidUpdate = new Employee(); // Missing required fields

        mockMvc.perform(put("/employees/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testDeleteEmployee_Success() throws Exception {
        long employeeId = employee.getId();

        mockMvc.perform(delete("/employees/" + employeeId))
                .andExpect(status().isOk());

        // Verify the employee has been deleted
        mockMvc.perform(get("/employees/" + employeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testDeleteEmployee_NotFound() throws Exception {
        // Try deleting an employee that doesn't exist
        mockMvc.perform(delete("/employees/999"))
                .andExpect(status().isNotFound());
    }
}