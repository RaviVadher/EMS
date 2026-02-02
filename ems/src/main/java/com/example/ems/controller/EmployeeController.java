package com.example.ems.controller;
import com.example.ems.dto.EmployeeDepartmentUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ems.dto.UserDTO;
import com.example.ems.entity.Employee;
import com.example.ems.service.EmployeeService;
import com.example.ems.service.UsersService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final UsersService usersService;
    private final EmployeeService employeeService;

    public EmployeeController(UsersService usersService, EmployeeService employeeService) {
        this.usersService = usersService;
        this.employeeService = employeeService;
    }



    @GetMapping("/userDashboard")
    public ResponseEntity<String> userDashboard() {
        return ResponseEntity.ok("Welcome, User! You can view this content.");
    }


    @GetMapping("/{id}")
    public Employee getEmpById(@PathVariable int id){
        return employeeService.getEmployeeById(id);
    }


    @GetMapping("/allEmployee")
    public ResponseEntity<?> getAllEmployees() {
        return getResponseEntity(employeeService);
    }

    static ResponseEntity<?> getResponseEntity(EmployeeService employeeService) {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        Map<String, Object> response = new HashMap<>();
        response.put("count", allEmployees.size());
        response.put("employees", allEmployees);
        return ResponseEntity.ok(response);
    }

   @PostMapping("/addEmployee")
   public Employee addEmployee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
  }



    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Integer id, @Valid @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PatchMapping("/{id}/department")
    public ResponseEntity<Employee> updateEmployeeDepartment(
            @PathVariable Integer id,
            @Valid @RequestBody EmployeeDepartmentUpdateDTO updates) {
        Employee updatedEmployee = employeeService.updateEmployeeDepartment(id, updates);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Integer id) {

        boolean isDeleted = employeeService.deleteEmployee(id);

        if (isDeleted) {
            return ResponseEntity.ok("Employee with ID " + id + " deleted succesfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}