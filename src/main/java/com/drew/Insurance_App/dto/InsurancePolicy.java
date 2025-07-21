package com.drew.Insurance_App.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "insurancePolicy")
@Getter
@Setter
public class InsurancePolicy {
    @Id
    @Column(name = "policyId")
    private int insurancePolicyId;

    @NotBlank(message = "Policy number is required")
    private String insurancePolicyNumber;

    @NotBlank(message = "Policy type is required")
    private String insurancePolicyType;

    @Min(value = 1, message = "Coverage amount must be positive")
    private long insurancePolicyCoverageAmount;

    @NotBlank(message = "Policy premium is required")
    private String insurancePolicyPremium;

    @NotBlank(message = "Policy start date is required")
    private String insurancePolicyStartDate;

    @NotBlank(message = "Policy end date is required")
    private String insurancePolicyEndDate;
}
