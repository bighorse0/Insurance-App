package com.drew.Insurance_App.dto;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class ClaimAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int claimId;
    private String action;
    private Date timestamp;
    @Lob
    private String details;
    private String username;

    public ClaimAuditLog() {}
    public ClaimAuditLog(int claimId, String action, Date timestamp, String details, String username) {
        this.claimId = claimId;
        this.action = action;
        this.timestamp = timestamp;
        this.details = details;
        this.username = username;
    }
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getClaimId() { return claimId; }
    public void setClaimId(int claimId) { this.claimId = claimId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
} 