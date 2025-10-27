package com.example.ayush.controller;

import com.example.ayush.model.AyushRegistration;
import com.example.ayush.service.AyushRegistrationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private AyushRegistrationService registrationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest,
                                                    HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            
            AyushRegistration user = registrationService.findByUsername(username);
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                session.setAttribute("user", user);
                // Set Spring Security authentication
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
                response.put("status", "success");
                response.put("message", "Login successful");
                response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getOrganizationEmail(),
                    "fullName", user.getTrainerName()
                ));
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Invalid username or password");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Logout successful");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-session")
    public ResponseEntity<Map<String, Object>> checkSession(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        AyushRegistration user = (AyushRegistration) session.getAttribute("user");
        
        if (user != null) {
            response.put("status", "success");
            response.put("authenticated", true);
            response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getOrganizationEmail(),
                "fullName", user.getTrainerName()
            ));
        } else {
            response.put("status", "success");
            response.put("authenticated", false);
        }
        
        return ResponseEntity.ok(response);
    }
} 