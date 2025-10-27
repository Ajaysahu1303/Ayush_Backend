package com.example.ayush.repository;

import com.example.ayush.model.AyushApplication;
import com.example.ayush.model.AyushRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AyushApplicationRepository extends JpaRepository<AyushApplication, Long> {
    
    List<AyushApplication> findByUser(AyushRegistration user);
    
    Optional<AyushApplication> findByUserAndStatus(AyushRegistration user, AyushApplication.ApplicationStatus status);
    
    List<AyushApplication> findByStatus(AyushApplication.ApplicationStatus status);
    
    boolean existsByUser(AyushRegistration user);
    
    Optional<AyushApplication> findFirstByUserOrderByCreatedAtDesc(AyushRegistration user);
} 