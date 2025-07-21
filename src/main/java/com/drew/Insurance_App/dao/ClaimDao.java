package com.drew.Insurance_App.dao;

import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.repository.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class ClaimDao {
    @Autowired
    private ClaimRepository claimRepository;
    // CREATE
    public Claim insertClaim(Claim claim) {
        return claimRepository.save(claim);
    }

    // READ
    public Claim getByClaimId(int claimId) {
        Optional<Claim> claim = claimRepository.findById(claimId);

        return claim.orElse(null);
    }

    // UPDATE
    public Claim updateClaim(Claim claim) {
        return claimRepository.save(claim);
    }

    // DELETE
    public Claim deleteClaimById(int claimId) {
        Optional<Claim> claim = claimRepository.findById(claimId);

        if (claim.isPresent()) {
            claimRepository.deleteById(claimId);
            return claim.get();
        }

        return null;
    }

    public List<Claim> displayAll() {
        return claimRepository.findAll();
    }

    public List<Claim> findByPolicyId(int policyId) {
        return claimRepository.findByInsurancePolicy_InsurancePolicyIdAndIsDeletedFalse(policyId);
    }

    public List<Claim> findByClaimStatus(String claimStatus) {
        return claimRepository.findByClaimStatusAndIsDeletedFalse(claimStatus);
    }

    public List<Claim> findByPolicyIds(List<Integer> policyIds) {
        return claimRepository.findByInsurancePolicy_InsurancePolicyIdIn(policyIds);
    }

    public List<Claim> filterByDateRange(Date startDate, Date endDate) {
        return claimRepository.findByClaimDateBetweenAndIsDeletedFalse(startDate, endDate);
    }
    public List<Claim> filterByAmountRange(double minAmount, double maxAmount) {
        return claimRepository.findByClaimAmountBetweenAndIsDeletedFalse(minAmount, maxAmount);
    }
    public List<Claim> filterByStatus(String status) {
        return claimRepository.findByClaimStatusAndIsDeletedFalse(status);
    }
    public List<Claim> filterByAll(Date startDate, Date endDate, double minAmount, double maxAmount, String status) {
        return claimRepository.findByClaimDateBetweenAndClaimAmountBetweenAndClaimStatusAndIsDeletedFalse(startDate, endDate, minAmount, maxAmount, status);
    }

    public Claim save(Claim claim) {
        return claimRepository.save(claim);
    }
}
