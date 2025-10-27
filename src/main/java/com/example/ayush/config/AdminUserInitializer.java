package com.example.ayush.config;

import com.example.ayush.model.AdminUser;
import com.example.ayush.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {
    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String defaultUsername = "admin";
        String defaultPassword = "admin123";
        if (adminUserRepository.findByUsername(defaultUsername).isEmpty()) {
            AdminUser admin = new AdminUser();
            admin.setUsername(defaultUsername);
            admin.setPassword(passwordEncoder.encode(defaultPassword));
            adminUserRepository.save(admin);
            System.out.println("Default admin user created: username='admin', password='admin123'");
        }
    }
} 