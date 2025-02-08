package com.drew.Insurance_App.service;

import com.drew.Insurance_App.dao.ClaimDao;
import com.drew.Insurance_App.dao.InsurancePolicyDao;
import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.dto.InsurancePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClaimServiceImpl implements ClaimService {

    @Autowired
    private ClaimDao claimDao;

    @Autowired
    private InsurancePolicyDao insurancePolicyDao;

    @Autowired
    private ApiResponse<Claim> claimApiResponse;

    @Autowired
    private ApiResponse<List<Claim>> findAllClaimsApiResponse;

    @Override
    public ApiResponse<Claim> insertClaim(Claim claim, int policyId) {
        InsurancePolicy insurancePolicy = insurancePolicyDao.getByPolicyId(policyId);

        if (Objects.isNull(insurancePolicy)) {
            claimApiResponse.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            claimApiResponse.setMessage("Has not been claimed yet");
            claimApiResponse.setData(null);
        } else {
            claim.setInsurancePolicy(insurancePolicy);
            claimDao.insertClaim(claim);

            claimApiResponse.setStatus(HttpStatus.ACCEPTED.value());
            claimApiResponse.setMessage("Claim successful");
            claimApiResponse.setData(claim);
        }

        return claimApiResponse;
    }

    @Override
    public ApiResponse<Claim> getClaimById(int claimId) {
        Claim claim = claimDao.getByClaimId(claimId);

        if (Objects.isNull(claim)) {
            throw new RuntimeException("Claim not found");
        }

        claimApiResponse.setStatus(HttpStatus.ACCEPTED.value());
        claimApiResponse.setMessage("Claim exists");
        claimApiResponse.setData(claim);

        return claimApiResponse;
    }

    @Override
    public ApiResponse<Claim> updateClaim(Claim claim) {
        Claim updatedClaim = claimDao.getByClaimId(claim.getClaimId());

        if (Objects.isNull(updatedClaim)) {
            throw new RuntimeException("Claim not found");
        }

        updatedClaim.setClaimNumber(claim.getClaimNumber());
        updatedClaim.setClaimDate(claim.getClaimDate());
        updatedClaim.setClaimDescription(claim.getClaimDescription());
        updatedClaim.setClaimStatus(claim.getClaimStatus());

        claimDao.updateClaim(updatedClaim);

        claimApiResponse.setStatus(HttpStatus.ACCEPTED.value());
        claimApiResponse.setMessage("Claim updated successfully");
        claimApiResponse.setData(updatedClaim);

        return claimApiResponse;
    }

    @Override
    public ApiResponse<Claim> deleteClaim(int claimId) {
        Claim claim = claimDao.deleteClaimById(claimId);

        if (Objects.isNull(claim)) {
            throw new RuntimeException("Claim not found");
        }

        claimApiResponse.setStatus(HttpStatus.FOUND.value());
        claimApiResponse.setMessage("Claim deleted successfully");
        claimApiResponse.setData(claim);

        return claimApiResponse;
    }

    @Override
    public ApiResponse<List<Claim>> displayAll() {
        List<Claim> claims = claimDao.displayAll();

        if (Objects.isNull(claims)) {
            findAllClaimsApiResponse.setStatus(HttpStatus.NOT_FOUND.value());
            findAllClaimsApiResponse.setMessage("Claims not found");
            findAllClaimsApiResponse.setData(null);
        } else {
            findAllClaimsApiResponse.setStatus(HttpStatus.FOUND.value());
            findAllClaimsApiResponse.setMessage("All claims found");
            findAllClaimsApiResponse.setData(claims);
        }

        return findAllClaimsApiResponse;
    }
}
