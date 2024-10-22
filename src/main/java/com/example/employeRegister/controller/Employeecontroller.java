package com.example.employeRegister.controller;

import com.example.employeRegister.model.Employee;
import com.example.employeRegister.service.Employeeservice;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class Employeecontroller {

    @Autowired
    private Employeeservice employeeservice;

    // Post employee
    @PostMapping("/employees")
    public Employee postEmployee(@RequestBody Employee employee) {
        return employeeservice.postemploye(employee);
    }

    // Delete employee by ID
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            employeeservice.Deleteemploye(id);
            return new ResponseEntity<>("Employee with id " + id + " deleted successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Get employee by ID
    @GetMapping("/employee/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeservice.getEmplyeebyId(id);
        if (employee == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employee);
    }

    // Update employee
    @PatchMapping("/employee/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeservice.updateemployee(id, employee);
        if (updatedEmployee == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(updatedEmployee);
    }

    // Get employees with pagination and search by name----//
    @GetMapping("/employees/search")
    public ResponseEntity<Page<Employee>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees;

        // If a name is provided, search by name, otherwise return all employees
        if (name != null && !name.isEmpty()) {
            employees = employeeservice.searchEmployeesByName(name, pageable);
        } else {
            employees = (Page<Employee>) employeeservice.GetAllEmployee(pageable);
        }

        return ResponseEntity.ok(employees);
    }
    //*--------------- ALL EMPLOYEE -----------*//

    @GetMapping("/employees")
    public ResponseEntity<Page<Employee>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Employee> employees = employeeservice.getAllEmployees(pageable);
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception for debugging
            System.out.println("Error fetching employees: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
