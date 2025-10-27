package com.example.ayush.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ayush_applications")
public class AyushApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private AyushRegistration user;
    
    // Personal Details
    @NotBlank(message = "Full name is required")
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @NotBlank(message = "Date of birth is required")
    @Column(name = "date_of_birth", nullable = false)
    private String dateOfBirth;
    
    @NotBlank(message = "Gender is required")
    @Column(nullable = false)
    private String gender;
    
    @NotBlank(message = "Nationality is required")
    @Column(nullable = false)
    private String nationality;
    
    @NotBlank(message = "PAN number is required")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Please enter a valid PAN number")
    @Column(name = "pan_number", nullable = false)
    private String panNumber;
    
    @NotBlank(message = "Aadhaar number is required")
    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhaar number must be 12 digits")
    @Column(name = "aadhaar_number", nullable = false)
    private String aadhaarNumber;
    
    // Company Details
    @NotBlank(message = "Company name is required")
    @Column(name = "company_name", nullable = false)
    private String companyName;
    
    @NotBlank(message = "Company type is required")
    @Column(name = "company_type", nullable = false)
    private String companyType; // Proprietorship, Partnership, Private Limited, etc.
    
    @NotBlank(message = "Business category is required")
    @Column(name = "business_category", nullable = false)
    private String businessCategory; // Ayurveda, Yoga, Unani, Siddha, Homeopathy
    
    @NotBlank(message = "Company description is required")
    @Size(min = 50, message = "Company description must be at least 50 characters")
    @Column(name = "company_description", nullable = false, length = 2000)
    private String companyDescription;
    
    @NotBlank(message = "Business address is required")
    @Column(name = "business_address", nullable = false)
    private String businessAddress;
    
    @NotBlank(message = "Business phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @Column(name = "business_phone", nullable = false)
    private String businessPhone;
    
    @NotBlank(message = "Business email is required")
    @Email(message = "Please enter a valid email address")
    @Column(name = "business_email", nullable = false)
    private String businessEmail;
    
    @NotBlank(message = "GST number is required")
    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$", message = "Please enter a valid GST number")
    @Column(name = "gst_number", nullable = false)
    private String gstNumber;
    
    // Financial Details
    @NotNull(message = "Annual turnover is required")
    @Min(value = 0, message = "Annual turnover cannot be negative")
    @Column(name = "annual_turnover", nullable = false)
    private Double annualTurnover;
    
    @NotNull(message = "Investment amount is required")
    @Min(value = 0, message = "Investment amount cannot be negative")
    @Column(name = "investment_amount", nullable = false)
    private Double investmentAmount;
    
    // Document Uploads (File paths)
    @Column(name = "pan_card_path")
    private String panCardPath;
    
    @Column(name = "aadhaar_card_path")
    private String aadhaarCardPath;
    
    @Column(name = "business_plan_path")
    private String businessPlanPath;
    
    @Column(name = "financial_statement_path")
    private String financialStatementPath;
    
    @Column(name = "certificate_path")
    private String certificatePath;
    
    // Application Status
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.DRAFT;
    
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
    
    @Column(name = "reviewer_comments", length = 1000)
    private String reviewerComments;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Application Status Enum
    public enum ApplicationStatus {
        DRAFT("Draft"),
        SUBMITTED("Application Received"),
        IN_REVIEW("In Review"),
        APPROVED("Approved"),
        REJECTED("Rejected");
        
        private final String displayName;
        
        ApplicationStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public AyushRegistration getUser() {
        return user;
    }
    
    public void setUser(AyushRegistration user) {
        this.user = user;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public String getPanNumber() {
        return panNumber;
    }
    
    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }
    
    public String getAadhaarNumber() {
        return aadhaarNumber;
    }
    
    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getCompanyType() {
        return companyType;
    }
    
    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
    
    public String getBusinessCategory() {
        return businessCategory;
    }
    
    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }
    
    public String getCompanyDescription() {
        return companyDescription;
    }
    
    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }
    
    public String getBusinessAddress() {
        return businessAddress;
    }
    
    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }
    
    public String getBusinessPhone() {
        return businessPhone;
    }
    
    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }
    
    public String getBusinessEmail() {
        return businessEmail;
    }
    
    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }
    
    public String getGstNumber() {
        return gstNumber;
    }
    
    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }
    
    public Double getAnnualTurnover() {
        return annualTurnover;
    }
    
    public void setAnnualTurnover(Double annualTurnover) {
        this.annualTurnover = annualTurnover;
    }
    
    public Double getInvestmentAmount() {
        return investmentAmount;
    }
    
    public void setInvestmentAmount(Double investmentAmount) {
        this.investmentAmount = investmentAmount;
    }
    
    public String getPanCardPath() {
        return panCardPath;
    }
    
    public void setPanCardPath(String panCardPath) {
        this.panCardPath = panCardPath;
    }
    
    public String getAadhaarCardPath() {
        return aadhaarCardPath;
    }
    
    public void setAadhaarCardPath(String aadhaarCardPath) {
        this.aadhaarCardPath = aadhaarCardPath;
    }
    
    public String getBusinessPlanPath() {
        return businessPlanPath;
    }
    
    public void setBusinessPlanPath(String businessPlanPath) {
        this.businessPlanPath = businessPlanPath;
    }
    
    public String getFinancialStatementPath() {
        return financialStatementPath;
    }
    
    public void setFinancialStatementPath(String financialStatementPath) {
        this.financialStatementPath = financialStatementPath;
    }
    
    public String getCertificatePath() {
        return certificatePath;
    }
    
    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }
    
    public ApplicationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }
    
    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
    
    public String getReviewerComments() {
        return reviewerComments;
    }
    
    public void setReviewerComments(String reviewerComments) {
        this.reviewerComments = reviewerComments;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 