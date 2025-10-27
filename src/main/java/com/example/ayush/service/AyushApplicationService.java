package com.example.ayush.service;

import com.example.ayush.model.AyushApplication;
import com.example.ayush.model.AyushRegistration;
import com.example.ayush.model.ApplicationStatusHistory;
import com.example.ayush.repository.AyushApplicationRepository;
import com.example.ayush.repository.ApplicationStatusHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AyushApplicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(AyushApplicationService.class);
    
    @Autowired
    private AyushApplicationRepository applicationRepository;
    
    @Autowired
    private ApplicationStatusHistoryRepository statusHistoryRepository;
    
    private static final String UPLOAD_DIR = "uploads/applications/";
    
    @Transactional
    public AyushApplication saveOrUpdateApplication(AyushApplication application) {
        // Do not dereference lazy user proxy here; entity may be detached
        logger.info("Saving/updating application with ID: {}", application.getId());
        
        application.setUpdatedAt(LocalDateTime.now());
        
        try {
            AyushApplication savedApplication = applicationRepository.save(application);
            logger.info("Successfully saved application with ID: {}", savedApplication.getId());
            return savedApplication;
        } catch (Exception e) {
            logger.error("Failed to save application for user {}: {}", 
                application.getUser().getUsername(), e.getMessage());
            throw new RuntimeException("Failed to save application: " + e.getMessage());
        }
    }
    
    @Transactional
    public AyushApplication submitApplication(Long applicationId) {
        logger.info("Submitting application with ID: {}", applicationId);
        
        Optional<AyushApplication> optionalApplication = applicationRepository.findById(applicationId);
        if (optionalApplication.isEmpty()) {
            throw new RuntimeException("Application not found");
        }
        
        AyushApplication application = optionalApplication.get();
        // Ensure required documents are uploaded before allowing submission
        if (application.getPanCardPath() == null || application.getPanCardPath().trim().isEmpty()) {
            throw new RuntimeException("PAN card document is required before submission");
        }
        if (application.getAadhaarCardPath() == null || application.getAadhaarCardPath().trim().isEmpty()) {
            throw new RuntimeException("Aadhaar card document is required before submission");
        }
        application.setStatus(AyushApplication.ApplicationStatus.SUBMITTED);
        application.setSubmittedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());
        
        try {
            AyushApplication savedApplication = applicationRepository.save(application);
            logger.info("Successfully submitted application with ID: {}", savedApplication.getId());
            return savedApplication;
        } catch (Exception e) {
            logger.error("Failed to submit application with ID {}: {}", applicationId, e.getMessage());
            throw new RuntimeException("Failed to submit application: " + e.getMessage());
        }
    }
    
    public List<AyushApplication> getApplicationsByUser(AyushRegistration user) {
        logger.info("Getting applications for user: {}", user.getUsername());
        return applicationRepository.findByUser(user);
    }
    
    public Optional<AyushApplication> getLatestApplicationByUser(AyushRegistration user) {
        logger.info("Getting latest application for user: {}", user.getUsername());
        return applicationRepository.findFirstByUserOrderByCreatedAtDesc(user);
    }
    
    public Optional<AyushApplication> getApplicationById(Long applicationId) {
        logger.info("Getting application by ID: {}", applicationId);
        return applicationRepository.findById(applicationId);
    }
    
    public boolean hasExistingApplication(AyushRegistration user) {
        return applicationRepository.existsByUser(user);
    }
    
    @Transactional
    public AyushApplication updateApplicationStatus(Long applicationId, 
                                                   AyushApplication.ApplicationStatus status, 
                                                   String reviewerComments) {
        logger.info("Updating application status to {} for application ID: {}", status, applicationId);
        
        Optional<AyushApplication> optionalApplication = applicationRepository.findById(applicationId);
        if (optionalApplication.isEmpty()) {
            throw new RuntimeException("Application not found");
        }
        
        AyushApplication application = optionalApplication.get();
        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());
        
        if (status == AyushApplication.ApplicationStatus.IN_REVIEW || 
            status == AyushApplication.ApplicationStatus.APPROVED || 
            status == AyushApplication.ApplicationStatus.REJECTED) {
            application.setReviewedAt(LocalDateTime.now());
        }
        
        if (reviewerComments != null && !reviewerComments.trim().isEmpty()) {
            application.setReviewerComments(reviewerComments);
        }
        
        try {
            AyushApplication savedApplication = applicationRepository.save(application);
            // Save status history
            ApplicationStatusHistory history = new ApplicationStatusHistory();
            history.setApplication(savedApplication);
            history.setStatus(status);
            history.setUpdatedAt(LocalDateTime.now());
            statusHistoryRepository.save(history);
            logger.info("Successfully updated application status to {} for application ID: {}", 
                status, savedApplication.getId());
            return savedApplication;
        } catch (Exception e) {
            logger.error("Failed to update application status for ID {}: {}", applicationId, e.getMessage());
            throw new RuntimeException("Failed to update application status: " + e.getMessage());
        }
    }
    
    public String uploadDocument(MultipartFile file, String documentType, Long applicationId) {
        logger.info("Uploading document of type {} for application ID: {}", documentType, applicationId);
        
        try {
            // Create upload directory if it doesn't exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // Generate unique filename
            String originalFilename = java.util.Optional.ofNullable(file.getOriginalFilename()).orElse("uploaded");
            int dotIndex = originalFilename.lastIndexOf('.');
            String fileExtension = dotIndex >= 0 ? originalFilename.substring(dotIndex) : "";
            String uniqueFilename = UUID.randomUUID().toString() + "_" + documentType + fileExtension;
            
            // Save file
            Path filePath = Paths.get(UPLOAD_DIR + uniqueFilename);
            Files.write(filePath, file.getBytes());
            
            logger.info("Successfully uploaded document: {}", uniqueFilename);
            return uniqueFilename;
            
        } catch (IOException e) {
            logger.error("Failed to upload document for application ID {}: {}", applicationId, e.getMessage());
            throw new RuntimeException("Failed to upload document: " + e.getMessage());
        }
    }
    
    public byte[] downloadDocument(String filename) {
        logger.info("Downloading document: {}", filename);
        
        try {
            Path filePath = Paths.get(UPLOAD_DIR + filename);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            logger.error("Failed to download document {}: {}", filename, e.getMessage());
            throw new RuntimeException("Failed to download document: " + e.getMessage());
        }
    }
    
    public List<AyushApplication> getApplicationsByStatus(AyushApplication.ApplicationStatus status) {
        logger.info("Getting applications with status: {}", status);
        return applicationRepository.findByStatus(status);
    }
    
    public void deleteApplication(Long applicationId) {
        logger.info("Deleting application with ID: {}", applicationId);
        
        Optional<AyushApplication> optionalApplication = applicationRepository.findById(applicationId);
        if (optionalApplication.isEmpty()) {
            throw new RuntimeException("Application not found");
        }
        
        AyushApplication application = optionalApplication.get();
        
        // Delete associated files
        deleteApplicationFiles(application);
        
        try {
            applicationRepository.delete(application);
            logger.info("Successfully deleted application with ID: {}", applicationId);
        } catch (Exception e) {
            logger.error("Failed to delete application with ID {}: {}", applicationId, e.getMessage());
            throw new RuntimeException("Failed to delete application: " + e.getMessage());
        }
    }
    
    private void deleteApplicationFiles(AyushApplication application) {
        try {
            // Delete uploaded files
            if (application.getPanCardPath() != null) {
                Files.deleteIfExists(Paths.get(UPLOAD_DIR + application.getPanCardPath()));
            }
            if (application.getAadhaarCardPath() != null) {
                Files.deleteIfExists(Paths.get(UPLOAD_DIR + application.getAadhaarCardPath()));
            }
            if (application.getBusinessPlanPath() != null) {
                Files.deleteIfExists(Paths.get(UPLOAD_DIR + application.getBusinessPlanPath()));
            }
            if (application.getFinancialStatementPath() != null) {
                Files.deleteIfExists(Paths.get(UPLOAD_DIR + application.getFinancialStatementPath()));
            }
            if (application.getCertificatePath() != null) {
                Files.deleteIfExists(Paths.get(UPLOAD_DIR + application.getCertificatePath()));
            }
        } catch (IOException e) {
            logger.warn("Failed to delete some application files: {}", e.getMessage());
        }
    }

    public List<AyushApplication> getAllApplications() {
        return applicationRepository.findAll();
    }
} 