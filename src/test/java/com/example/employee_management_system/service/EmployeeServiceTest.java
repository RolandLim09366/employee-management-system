package com.example.employee_management_system.service;

import com.example.employee_management_system.model.Department;
import com.example.employee_management_system.model.Employee;
import com.example.employee_management_system.repository.EmployeeRepo;
import com.example.employee_management_system.repository.DepartmentRepo;
import com.example.employee_management_system.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepo employeeRepo;

    @Mock
    private DepartmentRepo departmentRepo;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("IT");

        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setPosition("Developer");
        employee.setDepartment(department);
    }

    @Test
    void testGetAllEmployees() {
        when(employeeRepo.findAll()).thenReturn(Collections.singletonList(employee));

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetEmployeeById() {
        when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(1L));
    }

    @Test
    void testCreateEmployee() {
        when(departmentRepo.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);

        Employee savedEmployee = employeeService.createEmployee(employee);

        assertNotNull(savedEmployee);
        assertEquals("John Doe", savedEmployee.getName());
        verify(departmentRepo).findById(1L);
        verify(employeeRepo).save(any(Employee.class));
    }

    @Test
    void testCreateEmployee_InvalidData() {
        when(departmentRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.createEmployee(employee));
        verify(employeeRepo, never()).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee() {
        Employee updatedDetails = new Employee();
        updatedDetails.setId(1L);
        updatedDetails.setName("John Smith");
        updatedDetails.setPosition("Senior Developer");
        updatedDetails.setDepartment(department);

        when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));
        when(departmentRepo.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepo.save(any(Employee.class))).thenReturn(updatedDetails);

        Employee updatedEmployee = employeeService.updateEmployee(1L, updatedDetails);

        assertNotNull(updatedEmployee);
        assertEquals("John Smith", updatedEmployee.getName());
        assertEquals("Senior Developer", updatedEmployee.getPosition());
    }

    @Test
    void testUpdateEmployee_InvalidData() {
        when(employeeRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.updateEmployee(1L, employee));
    }

    @Test
    void testDeleteEmployee() {
        when(employeeRepo.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepo).deleteById(1L);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeRepo.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(1L));
    }
}
