package com.example.ayush.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "application_status_history")
public class ApplicationStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private AyushApplication application;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AyushApplication.ApplicationStatus status;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AyushApplication getApplication() { return application; }
    public void setApplication(AyushApplication application) { this.application = application; }
    public AyushApplication.ApplicationStatus getStatus() { return status; }
    public void setStatus(AyushApplication.ApplicationStatus status) { this.status = status; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 