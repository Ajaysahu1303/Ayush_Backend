package com.example.ayush.repository;

import com.example.ayush.model.AyushRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AyushRegistrationRepository extends JpaRepository<AyushRegistration, Long> {
    Optional<AyushRegistration> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByOrganizationEmail(String email);
} 