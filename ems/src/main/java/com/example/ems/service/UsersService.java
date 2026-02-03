package com.example.ems.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ems.dto.UserDTO;
import com.example.ems.entity.Users;
import com.example.ems.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Users registerUser(Users user) {
        if (usersRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already taken");
        } else if (user.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }
        user.setCreatedAt(LocalDateTime.now());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return usersRepository.save(user);
    }

    public Users findByUsername(String username) {
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}