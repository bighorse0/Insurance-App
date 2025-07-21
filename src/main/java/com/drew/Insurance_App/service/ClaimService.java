package com.drew.Insurance_App.service;

import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.Claim;

import java.util.Date;
import java.util.List;

public interface ClaimService {
    public ApiResponse<Claim> insertClaim(Claim claim, int policyId);
    public ApiResponse<Claim> getClaimById(int claimId);
    public ApiResponse<Claim> updateClaim(Claim claim);
    public ApiResponse<Claim> deleteClaim(int claimId);
    public ApiResponse<List<Claim>> displayAll();
    ApiResponse<List<Claim>> getClaimsByPolicyId(int policyId);
    ApiResponse<List<Claim>> getClaimsByStatus(String claimStatus);
    ApiResponse<List<Claim>> getClaimsByPolicyIds(List<Integer> policyIds);
    ApiResponse<List<Claim>> filterByDateRange(Date startDate, Date endDate);
    ApiResponse<List<Claim>> filterByAmountRange(double minAmount, double maxAmount);
    ApiResponse<List<Claim>> filterByStatus(String status);
    ApiResponse<List<Claim>> filterByAll(Date startDate, Date endDate, double minAmount, double maxAmount, String status);
}
