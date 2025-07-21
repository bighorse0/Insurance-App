package com.drew.Insurance_App.controller;

import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.Validation;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @PostMapping("/createClaim/{policyId}")
    public ResponseEntity<ApiResponse<Claim>> createClaim(@Valid @RequestBody Claim claim, @PathVariable int policyId) {
        ApiResponse<Claim> response = claimService.insertClaim(claim, policyId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/bulkCreateClaims/{policyId}")
    public ResponseEntity<List<ApiResponse<Claim>>> bulkCreateClaims(@RequestBody List<Claim> claims, @PathVariable int policyId) {
        List<ApiResponse<Claim>> responses = new ArrayList<>();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        for (Claim claim : claims) {
            try {
                Set<ConstraintViolation<Claim>> violations = validator.validate(claim);
                if (!violations.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (ConstraintViolation<Claim> v : violations) sb.append(v.getPropertyPath()).append(": ").append(v.getMessage()).append("; ");
                    ApiResponse<Claim> error = new ApiResponse<>();
                    error.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                    error.setMessage(sb.toString());
                    error.setData(null);
                    responses.add(error);
                    continue;
                }
                responses.add(claimService.insertClaim(claim, policyId));
            } catch (Exception e) {
                ApiResponse<Claim> error = new ApiResponse<>();
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                error.setMessage("Exception: " + e.getMessage());
                error.setData(null);
                responses.add(error);
            }
        }
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/getClaim/{claimId}")
    public ResponseEntity<ApiResponse<Claim>> getClaimById(@PathVariable int claimId) {
        ApiResponse<Claim> response = claimService.getClaimById(claimId);
        if (response.getStatus() == 404) {
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateClaim/{claimId}")
    public ResponseEntity<ApiResponse<Claim>> updateClaim(@Valid @RequestBody Claim claim) {
        ApiResponse<Claim> response = claimService.updateClaim(claim);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/deleteClaim/{claimId}")
    public ResponseEntity<ApiResponse<Claim>> deleteClaimById(@PathVariable int claimId) {
        ApiResponse<Claim> response = claimService.deleteClaim(claimId);
        if (response.getStatus() == 404) {
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/restoreClaim/{claimId}")
    public ResponseEntity<ApiResponse<Claim>> restoreClaim(@PathVariable int claimId) {
        ApiResponse<Claim> response = ((com.drew.Insurance_App.service.ClaimServiceImpl) claimService).restoreClaim(claimId);
        if (response.getStatus() == 404) {
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/displayAllClaims")
    public ApiResponse<List<Claim>> displayAllClaims() {
        return claimService.displayAll();
    }

    @GetMapping("/claimsByPolicy/{policyId}")
    public ApiResponse<List<Claim>> getClaimsByPolicyId(@PathVariable int policyId) {
        return claimService.getClaimsByPolicyId(policyId);
    }

    @GetMapping("/claimsByStatus/{claimStatus}")
    public ApiResponse<List<Claim>> getClaimsByStatus(@PathVariable String claimStatus) {
        return claimService.getClaimsByStatus(claimStatus);
    }

    @GetMapping("/claims/filter")
    public ResponseEntity<ApiResponse<List<Claim>>> filterClaims(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) String status) {
        if (startDate != null && endDate != null && minAmount != null && maxAmount != null && status != null) {
            return ResponseEntity.ok(claimService.filterByAll(startDate, endDate, minAmount, maxAmount, status));
        } else if (startDate != null && endDate != null) {
            return ResponseEntity.ok(claimService.filterByDateRange(startDate, endDate));
        } else if (minAmount != null && maxAmount != null) {
            return ResponseEntity.ok(claimService.filterByAmountRange(minAmount, maxAmount));
        } else if (status != null) {
            return ResponseEntity.ok(claimService.filterByStatus(status));
        } else {
            ApiResponse<List<Claim>> response = new ApiResponse<>();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("At least one filter parameter is required");
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; "));
        response.setMessage(sb.toString());
        response.setData(null);
        return ResponseEntity.badRequest().body(response);
    }
}
