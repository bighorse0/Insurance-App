package com.drew.Insurance_App.service;

import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.Claim;

import java.util.List;

public interface ClaimService {
    public ApiResponse<Claim> insertClaim(Claim claim, int policyId);
    public ApiResponse<Claim> getClaimById(int claimId);
    public ApiResponse<Claim> updateClaim(Claim claim);
    public ApiResponse<Claim> deleteClaim(int claimId);
    public ApiResponse<List<Claim>> displayAll();
}
