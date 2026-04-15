package com.saas.backend.controller;

import com.saas.backend.entity.User;
import com.saas.backend.repository.UserRepository;
import com.saas.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    //  REGISTER USER
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {

        Optional<User> existing = userRepository.findByEmail(user.getEmail());

        Map<String, String> response = new HashMap<>();
        if (existing.isPresent()) {
            response.put("message", "User already exists!");
            return ResponseEntity.badRequest().body(response);
        }

        // Set default role
        if (user.getRole() == null) {
            user.setRole("USER"); // default
        }

        try {
            userRepository.save(user);
        } catch (Exception e) {
            response.put("message", "Database Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }

        response.put("message", "User registered successfully!");
        return ResponseEntity.ok(response);
    }

    //  LOGIN USER
    @PostMapping("/login")
    public org.springframework.http.ResponseEntity<java.util.Map<String, String>> login(@RequestBody User user) {

        User existing = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!existing.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        //  Generate JWT token
        String token = jwtUtil.generateToken(existing.getEmail());

        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("token", token);
        response.put("role", existing.getRole());

        return org.springframework.http.ResponseEntity.ok(response);
    }
}