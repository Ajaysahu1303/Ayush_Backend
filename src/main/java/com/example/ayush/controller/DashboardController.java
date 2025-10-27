package com.example.ayush.controller;

import com.example.ayush.model.AyushApplication;
import com.example.ayush.model.AyushRegistration;
import com.example.ayush.service.AyushApplicationService;
import com.example.ayush.dto.ApplicationDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import java.security.Principal;
import com.example.ayush.service.AyushRegistrationService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import java.security.Principal;
import com.example.ayush.service.AyushRegistrationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private AyushApplicationService applicationService;

    @Autowired
    private AyushRegistrationService registrationService;

    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getDashboardData(Principal principal) {
        // Debug log for authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("[DEBUG] Current auth in /api/dashboard/data: " + auth);
        Map<String, Object> response = new HashMap<>();
        if (principal == null) {
            response.put("status", "error");
            response.put("message", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String username = principal.getName();
        AyushRegistration user = registrationService.findByUsername(username);
        if (user == null) {
            response.put("status", "error");
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // Get user's latest application
        Optional<AyushApplication> latestApplication = applicationService.getLatestApplicationByUser(user);
        if (latestApplication.isPresent()) {
            response.put("hasApplication", true);
            AyushApplication app = latestApplication.get();
            ApplicationDTO appDto = new ApplicationDTO();
            appDto.setId(app.getId());
            appDto.setFullName(app.getFullName());
            appDto.setDateOfBirth(app.getDateOfBirth());
            appDto.setGender(app.getGender());
            appDto.setNationality(app.getNationality());
            appDto.setPanNumber(app.getPanNumber());
            appDto.setAadhaarNumber(app.getAadhaarNumber());
            appDto.setCompanyName(app.getCompanyName());
            appDto.setCompanyType(app.getCompanyType());
            appDto.setBusinessCategory(app.getBusinessCategory());
            appDto.setGstNumber(app.getGstNumber());
            appDto.setBusinessEmail(app.getBusinessEmail());
            appDto.setBusinessPhone(app.getBusinessPhone());
            appDto.setBusinessAddress(app.getBusinessAddress());
            appDto.setCompanyDescription(app.getCompanyDescription());
            appDto.setInvestmentAmount(app.getInvestmentAmount());
            appDto.setAnnualTurnover(app.getAnnualTurnover());
            appDto.setStatus(app.getStatus() != null ? app.getStatus().toString() : null);
            // ... set other fields as needed
            response.put("application", appDto);
        } else {
            response.put("hasApplication", false);
        }
        response.put("status", "success");
        response.put("user", Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "email", user.getOrganizationEmail(),
            "fullName", user.getTrainerName(),
            "category", user.getCategory(),
            "organizationPhone", user.getOrganizationPhone(),
            "country", user.getCountry(),
            "state", user.getState(),
            "city", user.getCity()
        ));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfileData(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Object userObj = session.getAttribute("user");
        
        if (userObj == null) {
            response.put("status", "error");
            response.put("message", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        AyushRegistration user = (AyushRegistration) userObj;
        
        // Get user's applications and map to DTOs
        List<ApplicationDTO> appDtos = applicationService.getApplicationsByUser(user)
            .stream()
            .map(app -> {
                ApplicationDTO dto = new ApplicationDTO();
                dto.setId(app.getId());
                dto.setFullName(app.getFullName());
                dto.setDateOfBirth(app.getDateOfBirth());
                dto.setGender(app.getGender());
                dto.setNationality(app.getNationality());
                dto.setPanNumber(app.getPanNumber());
                dto.setAadhaarNumber(app.getAadhaarNumber());
                dto.setCompanyName(app.getCompanyName());
                dto.setCompanyType(app.getCompanyType());
                dto.setBusinessCategory(app.getBusinessCategory());
                dto.setGstNumber(app.getGstNumber());
                dto.setBusinessEmail(app.getBusinessEmail());
                dto.setBusinessPhone(app.getBusinessPhone());
                dto.setBusinessAddress(app.getBusinessAddress());
                dto.setCompanyDescription(app.getCompanyDescription());
                dto.setInvestmentAmount(app.getInvestmentAmount());
                dto.setAnnualTurnover(app.getAnnualTurnover());
                dto.setStatus(app.getStatus() != null ? app.getStatus().toString() : null);
                // ... set other fields as needed
                return dto;
            })
            .collect(Collectors.toList());
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("username", user.getUsername());
        userData.put("email", user.getOrganizationEmail());
        userData.put("fullName", user.getTrainerName());
        userData.put("category", user.getCategory());
        userData.put("organizationPhone", user.getOrganizationPhone());
        userData.put("country", user.getCountry());
        userData.put("state", user.getState());
        userData.put("city", user.getCity());
        userData.put("pinCode", user.getPinCode());
        userData.put("address", user.getAddress());
        
        response.put("status", "success");
        response.put("user", userData);
        response.put("applications", appDtos);
        
        return ResponseEntity.ok(response);
    }
} 