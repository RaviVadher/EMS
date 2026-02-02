package com.example.ems.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EmployeeDepartmentUpdateDTO {

    @NotBlank(message = "Department cannot be blank")
    @Size(min = 2, max = 50, message = "Department name must be between 2 and 50 characters")
    private String department;


}
