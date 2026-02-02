package com.example.ems.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ems.entity.Users;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
}
