package com.drew.Insurance_App.service;

import com.drew.Insurance_App.dao.ClaimDao;
import com.drew.Insurance_App.dao.InsurancePolicyDao;
import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.dto.ClaimAuditLog;
import com.drew.Insurance_App.dto.InsurancePolicy;
import com.drew.Insurance_App.repository.ClaimAuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Autowired
    private ClaimAuditLogRepository auditLogRepository;

    private void logAudit(int claimId, String action, Claim claim) {
        String details = claim != null ? claim.toString() : null;
        ClaimAuditLog log = new ClaimAuditLog(claimId, action, new Date(), details, null);
        auditLogRepository.save(log);
    }

    @Override
    public ApiResponse<Claim> insertClaim(Claim claim, int policyId) {
        InsurancePolicy insurancePolicy = insurancePolicyDao.getByPolicyId(policyId);
        if (Objects.isNull(insurancePolicy)) {
            claimApiResponse.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            claimApiResponse.setMessage("Has not been claimed yet");
            claimApiResponse.setData(null);
        } else if (claim.getClaimNumber() == null || claim.getClaimDescription() == null || claim.getClaimDate() == null || claim.getClaimStatus() == null || claim.getClaimAmount() < 0) {
            claimApiResponse.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            claimApiResponse.setMessage("Validation error: missing or invalid fields");
            claimApiResponse.setData(null);
        } else {
            claim.setInsurancePolicy(insurancePolicy);
            claimDao.insertClaim(claim);
            claimApiResponse.setStatus(HttpStatus.ACCEPTED.value());
            claimApiResponse.setMessage("Claim successful");
            claimApiResponse.setData(claim);
        }
        logAudit(claim.getClaimId(), "CREATE", claim);
        return claimApiResponse;
    }

    @Override
    public ApiResponse<Claim> getClaimById(int claimId) {
        Claim claim = claimDao.getByClaimId(claimId);
        if (Objects.isNull(claim) || claim.isDeleted()) {
            claimApiResponse.setStatus(org.springframework.http.HttpStatus.NOT_FOUND.value());
            claimApiResponse.setMessage("Claim not found");
            claimApiResponse.setData(null);
            return claimApiResponse;
        }
        claimApiResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
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

        logAudit(claim.getClaimId(), "UPDATE", updatedClaim);

        return claimApiResponse;
    }

    @Override
    public ApiResponse<Claim> deleteClaim(int claimId) {
        Claim claim = claimDao.getByClaimId(claimId);
        if (Objects.isNull(claim)) {
            claimApiResponse.setStatus(org.springframework.http.HttpStatus.NOT_FOUND.value());
            claimApiResponse.setMessage("Claim not found");
            claimApiResponse.setData(null);
            return claimApiResponse;
        }
        claim.setDeleted(true);
        claimDao.save(claim);
        claimApiResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
        claimApiResponse.setMessage("Claim soft deleted successfully");
        claimApiResponse.setData(claim);
        logAudit(claimId, "DELETE", claim);
        return claimApiResponse;
    }

    public ApiResponse<Claim> restoreClaim(int claimId) {
        Claim claim = claimDao.getByClaimId(claimId);
        if (Objects.isNull(claim)) {
            claimApiResponse.setStatus(org.springframework.http.HttpStatus.NOT_FOUND.value());
            claimApiResponse.setMessage("Claim not found");
            claimApiResponse.setData(null);
            return claimApiResponse;
        }
        claim.setDeleted(false);
        claimDao.save(claim);
        claimApiResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
        claimApiResponse.setMessage("Claim restored successfully");
        claimApiResponse.setData(claim);
        logAudit(claimId, "RESTORE", claim);
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

    @Override
    public ApiResponse<List<Claim>> getClaimsByPolicyId(int policyId) {
        List<Claim> claims = claimDao.findByPolicyId(policyId);
        if (claims == null || claims.isEmpty()) {
            findAllClaimsApiResponse.setStatus(HttpStatus.NOT_FOUND.value());
            findAllClaimsApiResponse.setMessage("No claims found for this policy");
            findAllClaimsApiResponse.setData(null);
        } else {
            findAllClaimsApiResponse.setStatus(HttpStatus.FOUND.value());
            findAllClaimsApiResponse.setMessage("Claims found for policy");
            findAllClaimsApiResponse.setData(claims);
        }
        return findAllClaimsApiResponse;
    }

    @Override
    public ApiResponse<List<Claim>> getClaimsByStatus(String claimStatus) {
        List<Claim> claims = claimDao.findByClaimStatus(claimStatus);
        if (claims == null || claims.isEmpty()) {
            findAllClaimsApiResponse.setStatus(HttpStatus.NOT_FOUND.value());
            findAllClaimsApiResponse.setMessage("No claims found with this status");
            findAllClaimsApiResponse.setData(null);
        } else {
            findAllClaimsApiResponse.setStatus(HttpStatus.FOUND.value());
            findAllClaimsApiResponse.setMessage("Claims found with status");
            findAllClaimsApiResponse.setData(claims);
        }
        return findAllClaimsApiResponse;
    }

    @Override
    public ApiResponse<List<Claim>> getClaimsByPolicyIds(List<Integer> policyIds) {
        List<Claim> claims = claimDao.findByPolicyIds(policyIds);
        if (claims == null || claims.isEmpty()) {
            findAllClaimsApiResponse.setStatus(HttpStatus.NOT_FOUND.value());
            findAllClaimsApiResponse.setMessage("No claims found for these policies");
            findAllClaimsApiResponse.setData(null);
        } else {
            findAllClaimsApiResponse.setStatus(HttpStatus.FOUND.value());
            findAllClaimsApiResponse.setMessage("Claims found for policies");
            findAllClaimsApiResponse.setData(claims);
        }
        return findAllClaimsApiResponse;
    }

    @Override
    public ApiResponse<List<Claim>> filterByDateRange(Date startDate, Date endDate) {
        List<Claim> claims = claimDao.filterByDateRange(startDate, endDate);
        if (claims == null || claims.isEmpty()) {
            findAllClaimsApiResponse.setStatus(HttpStatus.NOT_FOUND.value());
            findAllClaimsApiResponse.setMessage("No claims found in date range");
            findAllClaimsApiResponse.setData(null);
        } else {
            findAllClaimsApiResponse.setStatus(HttpStatus.OK.value());
            findAllClaimsApiResponse.setMessage("Claims found in date range");
            findAllClaimsApiResponse.setData(claims);
        }
        return findAllClaimsApiResponse;
    }
    @Override
    public ApiResponse<List<Claim>> filterByAmountRange(double minAmount, double maxAmount) {
        List<Claim> claims = claimDao.filterByAmountRange(minAmount, maxAmount);
        if (claims == null || claims.isEmpty()) {
            findAllClaimsApiResponse.setStatus(HttpStatus.NOT_FOUND.value());
            findAllClaimsApiResponse.setMessage("No claims found in amount range");
            findAllClaimsApiResponse.setData(null);
        } else {
            findAllClaimsApiResponse.setStatus(HttpStatus.OK.value());
            findAllClaimsApiResponse.setMessage("Claims found in amount range");
            findAllClaimsApiResponse.setData(claims);
        }
        return findAllClaimsApiResponse;
    }
    @Override
    public ApiResponse<List<Claim>> filterByStatus(String status) {
        List<Claim> claims = claimDao.filterByStatus(status);
        if (claims == null || claims.isEmpty()) {
            findAllClaimsApiResponse.setStatus(HttpStatus.NOT_FOUND.value());
            findAllClaimsApiResponse.setMessage("No claims found with status");
            findAllClaimsApiResponse.setData(null);
        } else {
            findAllClaimsApiResponse.setStatus(HttpStatus.OK.value());
            findAllClaimsApiResponse.setMessage("Claims found with status");
            findAllClaimsApiResponse.setData(claims);
        }
        return findAllClaimsApiResponse;
    }
    @Override
    public ApiResponse<List<Claim>> filterByAll(Date startDate, Date endDate, double minAmount, double maxAmount, String status) {
        List<Claim> claims = claimDao.filterByAll(startDate, endDate, minAmount, maxAmount, status);
        if (claims == null || claims.isEmpty()) {
            findAllClaimsApiResponse.setStatus(HttpStatus.NOT_FOUND.value());
            findAllClaimsApiResponse.setMessage("No claims found with all filters");
            findAllClaimsApiResponse.setData(null);
        } else {
            findAllClaimsApiResponse.setStatus(HttpStatus.OK.value());
            findAllClaimsApiResponse.setMessage("Claims found with all filters");
            findAllClaimsApiResponse.setData(claims);
        }
        return findAllClaimsApiResponse;
    }
}
