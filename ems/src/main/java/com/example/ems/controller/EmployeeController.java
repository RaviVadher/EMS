package com.example.ems.controller;
import com.example.ems.dto.EmployeeDepartmentUpdateDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ems.dto.UserDTO;
import com.example.ems.entity.Employee;
import com.example.ems.service.EmployeeService;
import com.example.ems.service.UsersService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/v1/userDashboard")
    @Tag(name = "dashboard", description = "user dashboard")
    public ResponseEntity<String> userDashboard() {
        return ResponseEntity.ok("Welcome, User! You can view this content.");
    }


    @GetMapping("/{id}")
    @Tag(name = "getUserByID", description = "fetch employee by id")
    public Employee getEmpById(@PathVariable int id){
        return employeeService.getEmployeeById(id);
    }


    @GetMapping(value = "/allEmployee",params = "version=2")
    @Tag(name = "getEmployee -V2", description = "fetch all employee list")
    public ResponseEntity<?> getAllEmployees(@RequestParam("version") int version) {
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
   @Tag(name = "Add-Employee", description = "add the employee details")

   public Employee addEmployee(@RequestBody Employee employee){

        return employeeService.saveEmployee(employee);
  }



    @PutMapping("/{id}")
    @Tag(name = "Update-Employee", description = "upadate the employee details")

    public ResponseEntity<Employee> updateEmployee(@PathVariable Integer id, @Valid @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PatchMapping("/{id}/department")
    @Tag(name = "Patch-Mapping", description = "Update the department  of employee!")

    public ResponseEntity<Employee> updateEmployeeDepartment(
            @PathVariable Integer id,
            @Valid @RequestBody EmployeeDepartmentUpdateDTO updates) {
        Employee updatedEmployee = employeeService.updateEmployeeDepartment(id, updates);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    @Tag(name = "Delete-Employee", description = "pass the id of employee!")

    public ResponseEntity<String> deleteEmployee(@PathVariable Integer id) {

        boolean isDeleted = employeeService.deleteEmployee(id);

        if (isDeleted) {
            return ResponseEntity.ok("Employee with ID " + id + " deleted succesfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/upload")
    @Tag(name = "upload-Image", description = "upload image in png format!")
    public ResponseEntity<String> uploadImage(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }
            Path uploadDir = Paths.get("Uploads");
            Path targetPath = uploadDir.resolve("img" + id + ".png");
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("File saved as img" + id + ".png");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error saving file: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/image")
    @Tag(name = "get-image", description = "get image")
    public ResponseEntity<UrlResource> getImage(@PathVariable String id) {
        try {
            Path uploadDir = Paths.get("Uploads");
             System.out.println(uploadDir.toAbsolutePath());
              var files = Files.list(uploadDir);
                Path filePath = files
                        .filter(path -> path.getFileName().toString().startsWith("img"+ id))
                        .findFirst()
                        .orElse(null);
            System.out.println(files);
                UrlResource resource = new UrlResource(filePath.toUri());


                String contentType = Files.probeContentType(filePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }




}