package com.example.ems.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.ems.entity.Users;
import com.example.ems.config.JwtUtil;
import com.example.ems.service.UsersService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsersService usersService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UsersService usersService, JwtUtil jwtUtil) {
        this.usersService = usersService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "User login", description = "Authenticate user with username and password")
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletResponse response,  @RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Users user = usersService.findByUsername(username);

        if (user == null || !new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        Map<String, Object> responseDTO = new HashMap<>();
        responseDTO.put("token", token);
        responseDTO.put("username", user.getUsername());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid Users user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                    usersService.registerUser(user);
                    return ResponseEntity.ok("User registered successfully");


        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}

