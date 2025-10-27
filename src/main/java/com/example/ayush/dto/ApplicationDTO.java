package com.example.ayush.dto;

import java.time.LocalDate;

public class ApplicationDTO {
    private Long id;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String nationality;
    private String panNumber;
    private String aadhaarNumber;
    private String companyName;
    private String companyType;
    private String businessCategory;
    private String gstNumber;
    private String businessEmail;
    private String businessPhone;
    private String businessAddress;
    private String companyDescription;
    private Double investmentAmount;
    private Double annualTurnover;
    private String status;
    // Add other fields as needed

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }
    public String getAadhaarNumber() { return aadhaarNumber; }
    public void setAadhaarNumber(String aadhaarNumber) { this.aadhaarNumber = aadhaarNumber; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCompanyType() { return companyType; }
    public void setCompanyType(String companyType) { this.companyType = companyType; }
    public String getBusinessCategory() { return businessCategory; }
    public void setBusinessCategory(String businessCategory) { this.businessCategory = businessCategory; }
    public String getGstNumber() { return gstNumber; }
    public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }
    public String getBusinessEmail() { return businessEmail; }
    public void setBusinessEmail(String businessEmail) { this.businessEmail = businessEmail; }
    public String getBusinessPhone() { return businessPhone; }
    public void setBusinessPhone(String businessPhone) { this.businessPhone = businessPhone; }
    public String getBusinessAddress() { return businessAddress; }
    public void setBusinessAddress(String businessAddress) { this.businessAddress = businessAddress; }
    public String getCompanyDescription() { return companyDescription; }
    public void setCompanyDescription(String companyDescription) { this.companyDescription = companyDescription; }
    public Double getInvestmentAmount() { return investmentAmount; }
    public void setInvestmentAmount(Double investmentAmount) { this.investmentAmount = investmentAmount; }
    public Double getAnnualTurnover() { return annualTurnover; }
    public void setAnnualTurnover(Double annualTurnover) { this.annualTurnover = annualTurnover; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 