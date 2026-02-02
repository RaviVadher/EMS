package com.example.ems.service;
import com.example.ems.entity.Employee;
import com.example.ems.repository.EmployeeRepository;
import com.example.ems.dto.EmployeeDepartmentUpdateDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    //get all
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    //new employee
    public Employee saveEmployee(Employee employee) {
        employee.setHireDate(LocalDateTime.now());
        return employeeRepository.save(employee);
    }

    //find by id
    public Employee getEmployeeById(Integer id) {
        Employee emp = employeeRepository.findById(id).get();
        if (emp == null) {
            throw new RuntimeException("Employee not found");
        }
        return (emp);
    }

    //update
    public Employee updateEmployee(Integer id, Employee employeeDetails) {
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);

        if(existingEmployee== null)
        {
            throw new RuntimeException("Employee not found");
        }
        existingEmployee.setFullName(employeeDetails.getFullName());
        existingEmployee.setDepartment(employeeDetails.getDepartment());
        existingEmployee.setHireDate(employeeDetails.getHireDate());
        existingEmployee.setEmail(employeeDetails.getEmail());
        existingEmployee.setPhone(employeeDetails.getPhone());
        existingEmployee.setEmail(employeeDetails.getEmail());
        existingEmployee.setPosition(employeeDetails.getPosition());
        return employeeRepository.save(existingEmployee);

    }

    //Delete
    public boolean deleteEmployee(Integer id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //patch
    @Transactional
    public Employee updateEmployeeDepartment(Integer Id, EmployeeDepartmentUpdateDTO updateDto) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(Id);

        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setDepartment(updateDto.getDepartment());

            return employeeRepository.save(existingEmployee);
        } else {
            throw new RuntimeException("Employee not found");
        }
    }
}