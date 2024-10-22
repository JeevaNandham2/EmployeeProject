package com.example.employeRegister.service;

import com.example.employeRegister.model.Employee;
import com.example.employeRegister.repo.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Employeeservice {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee postemploye(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> GetAllEmployee(Pageable pageable) {
        return employeeRepository.findAll();
    }

    public void Deleteemploye(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee with id" + id + "Deleted");
        }
        employeeRepository.deleteById(id);
    }

    public Employee getEmplyeebyId(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee updateemployee(Long id, Employee employee) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee existingEmployee = employeeOptional.get();

            existingEmployee.setEmail(employee.getEmail());
            existingEmployee.setName(employee.getName());
            existingEmployee.setPhone(employee.getPhone());
            existingEmployee.setDepartment(employee.getDepartment());

            return employeeRepository.save(existingEmployee);
        }
        return null;
    }

    public Page<Employee> getEmployees(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // Correct Pageable
        return employeeRepository.findAll(pageable);
    }




    public int getTotalPagesForGroupedEmployees(int size) {
        long count = employeeRepository.count(); // Total employee count
        return (int) Math.ceil((double) count / size); // Calculate total pages
    }


    public Page<Employee> searchEmployeesByName(String name, Pageable pageable) {
        return employeeRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }




}
