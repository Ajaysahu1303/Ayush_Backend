package com.example.ayush.service;

import com.example.ayush.model.AdminUser;
import com.example.ayush.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminUserService {
    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<AdminUser> authenticate(String username, String password) {
        Optional<AdminUser> adminOpt = adminUserRepository.findByUsername(username);
        if (adminOpt.isPresent() && passwordEncoder.matches(password, adminOpt.get().getPassword())) {
            return adminOpt;
        }
        return Optional.empty();
    }
} 