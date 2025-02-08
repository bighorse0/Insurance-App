package com.drew.Insurance_App.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity

@Getter
@Setter
public class Claim {
    @Id
    @Column(name = "claimId")
    private int claimId;

    @Column(name = "claimNumber")
    private String claimNumber;

    @Column(name = "claimDescription")
    private String claimDescription;

    @Column(name = "claimDate")
    private Date claimDate;

    @Column(name = "claimStatus")
    private String claimStatus;

    @Column(name = "claimAmount")
    private double claimAmount;

    @OneToOne
    @JoinColumn(name = "policyId")
    private InsurancePolicy insurancePolicy;
}
