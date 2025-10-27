package com.example.ayush.service;

import com.example.ayush.model.AyushRegistration;
import com.example.ayush.repository.AyushRegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AyushRegistrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(AyushRegistrationService.class);
    
    @Autowired
    private AyushRegistrationRepository registrationRepository;

    @Transactional
    public AyushRegistration register(AyushRegistration registration) {
        logger.info("Starting registration process for username: {}", registration.getUsername());
        
        // Check if username already exists
        if (registrationRepository.existsByUsername(registration.getUsername())) {
            logger.warn("Username already exists: {}", registration.getUsername());
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (registrationRepository.existsByOrganizationEmail(registration.getOrganizationEmail())) {
            logger.warn("Email already registered: {}", registration.getOrganizationEmail());
            throw new RuntimeException("Email already registered");
        }

        try {
            logger.info("Saving registration to database for username: {}", registration.getUsername());
            AyushRegistration savedRegistration = registrationRepository.save(registration);
            logger.info("Successfully saved registration with ID: {}", savedRegistration.getId());
            return savedRegistration;
        } catch (Exception e) {
            logger.error("Failed to save registration for username {}: {}", registration.getUsername(), e.getMessage());
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public AyushRegistration findByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        return registrationRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User not found with username: {}", username);
                    return new RuntimeException("User not found");
                });
    }
} 