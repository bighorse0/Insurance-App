package com.drew.Insurance_App.repository;

import com.drew.Insurance_App.dto.ClaimAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimAuditLogRepository extends JpaRepository<ClaimAuditLog, Long> {
} 