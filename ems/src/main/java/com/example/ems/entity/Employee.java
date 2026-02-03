package com.example.ems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "name should be not empty")
    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @NotBlank(message = "department should be not empty")
    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String position;

    @Email(message = "email should be in right formte")
    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "hire_date")
    private LocalDateTime hireDate;


}


