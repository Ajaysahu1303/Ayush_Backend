package com.example.ayush.controller;

import com.example.ayush.model.AyushApplication;
import com.example.ayush.model.AyushRegistration;
import com.example.ayush.service.AyushApplicationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/application")
public class AyushApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(AyushApplicationController.class);

    @Autowired
    private AyushApplicationService applicationService;

    @GetMapping("/form-data")
    public ResponseEntity<Map<String, Object>> getApplicationFormData(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        AyushRegistration user = (AyushRegistration) session.getAttribute("user");
        
        if (user == null) {
            response.put("status", "error");
            response.put("message", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Check if user already has an application
        Optional<AyushApplication> existingApplication = applicationService.getLatestApplicationByUser(user);
        
        if (existingApplication.isPresent()) {
            AyushApplication application = existingApplication.get();
            response.put("hasExistingApplication", true);
            response.put("application", application);
            
            // If application is submitted or in review, return status
            if (application.getStatus() != AyushApplication.ApplicationStatus.DRAFT) {
                response.put("applicationStatus", application.getStatus().toString());
            }
        } else {
            response.put("hasExistingApplication", false);
        }

        // Add dropdown options
        response.put("genderOptions", List.of("Male", "Female", "Other"));
        response.put("nationalityOptions", List.of("Indian", "Other"));
        response.put("companyTypeOptions", List.of(
            "Proprietorship", "Partnership", "Private Limited", "Public Limited", 
            "Limited Liability Partnership", "Society", "Trust", "Other"
        ));
        response.put("businessCategoryOptions", List.of(
            "Ayurveda", "Yoga & Naturopathy", "Unani", "Siddha", "Homeopathy"
        ));

        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveApplication(@Valid @RequestBody AyushApplication application,
                                                              BindingResult result,
                                                              HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        AyushRegistration user = (AyushRegistration) session.getAttribute("user");
        
        if (user == null) {
            response.put("status", "error");
            response.put("message", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (result.hasErrors()) {
            logger.error("Validation errors in application form: {}", result.getAllErrors());
            response.put("status", "error");
            response.put("message", "Validation failed");
            response.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            application.setUser(user);
            application.setStatus(AyushApplication.ApplicationStatus.DRAFT);
            
            AyushApplication savedApplication = applicationService.saveOrUpdateApplication(application);
            logger.info("Successfully saved application with ID: {}", savedApplication.getId());
            
            response.put("status", "success");
            response.put("message", "Application saved successfully!");
            response.put("application", savedApplication);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to save application: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to save application: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitApplication(@RequestBody Map<String, Long> request,
                                                                HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        AyushRegistration user = (AyushRegistration) session.getAttribute("user");
        
        if (user == null) {
            response.put("status", "error");
            response.put("message", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Long applicationId = request.get("applicationId");
            AyushApplication submittedApplication = applicationService.submitApplication(applicationId);
            logger.info("Successfully submitted application with ID: {}", submittedApplication.getId());
            
            response.put("status", "success");
            response.put("message", "Application submitted successfully!");
            response.put("application", submittedApplication);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to submit application: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to submit application: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getApplicationStatus(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        AyushRegistration user = (AyushRegistration) session.getAttribute("user");
        
        if (user == null) {
            response.put("status", "error");
            response.put("message", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Optional<AyushApplication> application = applicationService.getLatestApplicationByUser(user);
        if (application.isPresent()) {
            response.put("status", "success");
            response.put("application", application.get());
        } else {
            response.put("status", "error");
            response.put("message", "No application found. Please create a new application.");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-document")
    public ResponseEntity<Map<String, Object>> uploadDocument(@RequestParam("file") MultipartFile file,
                                                             @RequestParam("documentType") String documentType,
                                                             @RequestParam("applicationId") Long applicationId,
                                                             HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        AyushRegistration user = (AyushRegistration) session.getAttribute("user");
        
        if (user == null) {
            response.put("status", "error");
            response.put("message", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            String filename = applicationService.uploadDocument(file, documentType, applicationId);
            
            // Update application with document path
            Optional<AyushApplication> application = applicationService.getApplicationById(applicationId);
            if (application.isPresent()) {
                AyushApplication app = application.get();
                switch (documentType) {
                    case "panCard":
                        app.setPanCardPath(filename);
                        break;
                    case "aadhaarCard":
                        app.setAadhaarCardPath(filename);
                        break;
                    case "businessPlan":
                        app.setBusinessPlanPath(filename);
                        break;
                    case "financialStatement":
                        app.setFinancialStatementPath(filename);
                        break;
                }
                // Only persist document path; do not touch lazy user here
                applicationService.saveOrUpdateApplication(app);
            }
            
            response.put("status", "success");
            response.put("message", "Document uploaded successfully");
            response.put("filename", filename);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to upload document: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to upload document: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/download-document/{filename}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String filename,
                                                  HttpSession session) {
        AyushRegistration user = (AyushRegistration) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            byte[] documentData = applicationService.downloadDocument(filename);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(documentData);
                    
        } catch (Exception e) {
            logger.error("Failed to download document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/edit")
    public ResponseEntity<Map<String, Object>> getApplicationForEdit(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        AyushRegistration user = (AyushRegistration) session.getAttribute("user");
        
        if (user == null) {
            response.put("status", "error");
            response.put("message", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Optional<AyushApplication> application = applicationService.getLatestApplicationByUser(user);
        if (application.isPresent()) {
            AyushApplication app = application.get();
            if (app.getStatus() == AyushApplication.ApplicationStatus.DRAFT) {
                response.put("status", "success");
                response.put("application", app);
            } else {
                response.put("status", "error");
                response.put("message", "Cannot edit submitted application");
            }
        } else {
            response.put("status", "error");
            response.put("message", "No application found");
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteApplication(@RequestBody Map<String, Long> request,
                                                                HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        AyushRegistration user = (AyushRegistration) session.getAttribute("user");
        
        if (user == null) {
            response.put("status", "error");
            response.put("message", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Long applicationId = request.get("applicationId");
            applicationService.deleteApplication(applicationId);
            
            response.put("status", "success");
            response.put("message", "Application deleted successfully!");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to delete application: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to delete application: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> getMyApplications(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        AyushRegistration user = (AyushRegistration) session.getAttribute("user");
        if (user == null) {
            response.put("status", "error");
            response.put("message", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        List<AyushApplication> applications = applicationService.getApplicationsByUser(user);
        response.put("status", "success");
        response.put("applications", applications);
        return ResponseEntity.ok(response);
    }
}