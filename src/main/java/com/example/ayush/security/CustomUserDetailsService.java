package com.example.ayush.security;

import com.example.ayush.model.AdminUser;
import com.example.ayush.model.AyushRegistration;
import com.example.ayush.repository.AdminUserRepository;
import com.example.ayush.repository.AyushRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private AyushRegistrationRepository ayushRegistrationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check admin first
        AdminUser admin = adminUserRepository.findByUsername(username).orElse(null);
        if (admin != null) {
            return new User(admin.getUsername(), admin.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }
        // Then check normal user
        AyushRegistration user = ayushRegistrationRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return new User(user.getUsername(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        }
        throw new UsernameNotFoundException("User not found");
    }
} 