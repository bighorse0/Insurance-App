package com.drew.Insurance_App.repository;

import com.drew.Insurance_App.dto.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Integer> {
    List<Claim> findByInsurancePolicy_InsurancePolicyIdAndIsDeletedFalse(int policyId);
    List<Claim> findByClaimStatusAndIsDeletedFalse(String claimStatus);
    List<Claim> findByInsurancePolicy_InsurancePolicyIdIn(List<Integer> policyIds);
    List<Claim> findByClaimDateBetweenAndIsDeletedFalse(Date startDate, Date endDate);
    List<Claim> findByClaimAmountBetweenAndIsDeletedFalse(double minAmount, double maxAmount);
    List<Claim> findByClaimDateBetweenAndClaimAmountBetweenAndClaimStatusAndIsDeletedFalse(Date startDate, Date endDate, double minAmount, double maxAmount, String status);
}
