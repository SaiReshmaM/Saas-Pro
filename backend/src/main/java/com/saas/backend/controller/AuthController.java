package com.saas.backend.controller;

import com.saas.backend.entity.User;
import com.saas.backend.repository.UserRepository;
import com.saas.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ REGISTER USER
    @PostMapping("/register")
    public String register(@RequestBody User user) {

        Optional<User> existing = userRepository.findByEmail(user.getEmail());

        if (existing.isPresent()) {
            return "User already exists!";
        }

        // ✅ FIX 1: Set default role
        if (user.getRole() == null) {
            user.setRole("USER"); // default
        }

        // ✅ FIX 2: Set tenantId (VERY IMPORTANT for SaaS)
        if (user.getTenantId() == null) {
            user.setTenantId(1L); // for now static (later dynamic)
        }

        userRepository.save(user);

        return "User registered successfully!";
    }

    // ✅ LOGIN USER
    @PostMapping("/login")
    public org.springframework.http.ResponseEntity<java.util.Map<String, String>> login(@RequestBody User user) {

        User existing = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!existing.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // ✅ Generate JWT token
        String token = jwtUtil.generateToken(existing.getEmail());

        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("token", token);
        response.put("role", existing.getRole());

        return org.springframework.http.ResponseEntity.ok(response);
    }
}