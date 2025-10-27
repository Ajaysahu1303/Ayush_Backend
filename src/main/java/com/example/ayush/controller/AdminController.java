package com.example.ayush.controller;

import com.example.ayush.dto.ApplicationDTO;
import com.example.ayush.model.AyushApplication;
import com.example.ayush.model.AdminUser;
import com.example.ayush.model.ApplicationStatusHistory;
import com.example.ayush.repository.ApplicationStatusHistoryRepository;
import com.example.ayush.service.AyushApplicationService;
import com.example.ayush.service.AdminUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AyushApplicationService applicationService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private ApplicationStatusHistoryRepository statusHistoryRepository;

    @GetMapping("/applications")
    public ResponseEntity<Map<String, Object>> getAllApplications() {
        Map<String, Object> response = new HashMap<>();
        List<ApplicationDTO> appDtos = applicationService.getAllApplications()
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
                // Add more fields as needed
                return dto;
            })
            .collect(Collectors.toList());
        response.put("status", "success");
        response.put("applications", appDtos);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        var adminOpt = adminUserService.authenticate(username, password);
        if (adminOpt.isPresent()) {
            session.setAttribute("admin", adminOpt.get());
            // Set Spring Security authentication for admin
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    adminOpt.get().getUsername(), null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );
            SecurityContextHolder.getContext().setAuthentication(authToken);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            response.put("status", "success");
            response.put("message", "Admin login successful");
        } else {
            response.put("status", "error");
            response.put("message", "Invalid admin credentials");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/application/update-status")
    public ResponseEntity<Map<String, Object>> updateApplicationStatus(@RequestBody Map<String, Object> req) {
        Long appId = Long.valueOf(req.get("applicationId").toString());
        String status = req.get("status").toString();
        applicationService.updateApplicationStatus(appId, AyushApplication.ApplicationStatus.valueOf(status), null);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/application/{id}/status-history")
    public ResponseEntity<List<Map<String, Object>>> getStatusHistory(@PathVariable Long id) {
        AyushApplication app = applicationService.getApplicationById(id)
            .orElseThrow(() -> new RuntimeException("Application not found"));
        List<ApplicationStatusHistory> historyList = statusHistoryRepository.findByApplicationOrderByUpdatedAtAsc(app);
        List<Map<String, Object>> result = historyList.stream().map(h -> {
            Map<String, Object> m = new HashMap<>();
            m.put("status", h.getStatus().toString());
            m.put("updatedAt", h.getUpdatedAt());
            return m;
        }).toList();
        return ResponseEntity.ok(result);
    }
} 