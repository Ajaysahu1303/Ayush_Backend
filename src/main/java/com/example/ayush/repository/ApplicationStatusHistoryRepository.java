package com.example.ayush.repository;

import com.example.ayush.model.ApplicationStatusHistory;
import com.example.ayush.model.AyushApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationStatusHistoryRepository extends JpaRepository<ApplicationStatusHistory, Long> {
    List<ApplicationStatusHistory> findByApplicationOrderByUpdatedAtAsc(AyushApplication application);
} 