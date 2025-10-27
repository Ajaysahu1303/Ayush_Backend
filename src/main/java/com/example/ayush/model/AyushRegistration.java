package com.example.ayush.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "ayush_registrations")
public class AyushRegistration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String category;
    
    @NotBlank(message = "Organization phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @Column(name = "organization_phone", nullable = false)
    private String organizationPhone;
    
    @NotBlank(message = "Organization email is required")
    @Email(message = "Please enter a valid email address")
    @Column(name = "organization_email", nullable = false)
    private String organizationEmail;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Column(nullable = false, unique = true)
    private String username;
    
    @NotBlank(message = "Trainer name is required")
    @Column(name = "trainer_name", nullable = false)
    private String trainerName;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(nullable = false)
    private String password;
    
    @Transient // This field won't be persisted in the database
    private String confirmPassword;
    
    @NotBlank(message = "Country is required")
    @Column(nullable = false)
    private String country;
    
    @NotBlank(message = "State is required")
    @Column(nullable = false)
    private String state;
    
    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;
    
    @NotBlank(message = "Pin code is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "Pin code must be 6 digits")
    @Column(name = "pin_code", nullable = false)
    private String pinCode;
    
    @NotBlank(message = "Address is required")
    @Size(min = 10, message = "Address must be at least 10 characters long")
    @Column(nullable = false)
    private String address;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrganizationPhone() {
        return organizationPhone;
    }

    public void setOrganizationPhone(String organizationPhone) {
        this.organizationPhone = organizationPhone;
    }

    public String getOrganizationEmail() {
        return organizationEmail;
    }

    public void setOrganizationEmail(String organizationEmail) {
        this.organizationEmail = organizationEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
} 