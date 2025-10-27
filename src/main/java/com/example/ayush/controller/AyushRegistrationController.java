package com.example.ayush.controller;

import com.example.ayush.model.AyushRegistration;
import com.example.ayush.service.AyushRegistrationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/registration")
public class AyushRegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(AyushRegistrationController.class);

    @Autowired
    private AyushRegistrationService registrationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody AyushRegistration registration,
                                                           BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        logger.info("Received registration request for username: {}", registration.getUsername());
        
        // Check if passwords match
        String confirmPassword = registration.getConfirmPassword();
        if (confirmPassword == null || !confirmPassword.equals(registration.getPassword())) {
            logger.warn("Password mismatch for username: {}", registration.getUsername());
            response.put("status", "error");
            response.put("message", "Passwords do not match");
            return ResponseEntity.badRequest().body(response);
        }

        if (result.hasErrors()) {
            logger.error("Validation errors for username {}: {}", registration.getUsername(), result.getAllErrors());
            response.put("status", "error");
            response.put("message", "Validation failed");
            response.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }

        // Encode the password before saving
        registration.setPassword(passwordEncoder.encode(registration.getPassword()));

        try {
            logger.info("Attempting to register user: {}", registration.getUsername());
            AyushRegistration savedRegistration = registrationService.register(registration);
            logger.info("Successfully registered user with ID: {}", savedRegistration.getId());
            
            response.put("status", "success");
            response.put("message", "Registration successful!");
            response.put("user", Map.of(
                "id", savedRegistration.getId(),
                "username", savedRegistration.getUsername(),
                "email", savedRegistration.getOrganizationEmail(),
                "fullName", savedRegistration.getTrainerName()
            ));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Registration failed for username {}: {}", registration.getUsername(), e.getMessage());
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 