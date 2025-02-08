package com.drew.Insurance_App.controller;

import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @PostMapping("/createClaim/{policyId}")
    public ApiResponse<Claim> createClaim(@RequestBody Claim claim, @PathVariable int policyId) {
        return claimService.insertClaim(claim, policyId);
    }

    @GetMapping("/getClaim/{claimId}")
    public ApiResponse<Claim> getClaimById(@PathVariable int claimId) {
        return claimService.getClaimById(claimId);
    }

    @PutMapping("/updateClaim/{claimId}")
    public ApiResponse<Claim> updateClaim(@RequestBody Claim claim) {
        return claimService.updateClaim(claim);
    }

    @DeleteMapping("/deleteClaim/{claimId}")
    public ApiResponse<Claim> deleteClaimById(@PathVariable int claimId) {
        return claimService.deleteClaim(claimId);
    }

    @GetMapping("/displayAllClaims")
    public ApiResponse<List<Claim>> displayAllClaims() {
        return claimService.displayAll();
    }
}
