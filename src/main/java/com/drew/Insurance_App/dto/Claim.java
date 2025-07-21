package com.drew.Insurance_App.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import jakarta.validation.constraints.*;

@Entity

@Getter
@Setter
public class Claim {
    @Id
    @Column(name = "claimId")
    private int claimId;

    @NotNull(message = "Claim number is required")
    private String claimNumber;

    @NotNull(message = "Claim description is required")
    private String claimDescription;

    @NotNull(message = "Claim date is required")
    private Date claimDate;

    @NotNull(message = "Claim status is required")
    private String claimStatus;

    @Min(value = 0, message = "Claim amount must be non-negative")
    private double claimAmount;

    private boolean isDeleted = false;
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    @ManyToOne
    @JoinColumn(name = "policyId")
    private InsurancePolicy insurancePolicy;
}
