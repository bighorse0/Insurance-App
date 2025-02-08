package com.drew.Insurance_App.service;

import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.InsurancePolicy;

import java.util.List;

public interface InsurancePolicyService {
    public ApiResponse<InsurancePolicy> insertPolicy(InsurancePolicy insurancePolicy);
    public ApiResponse<InsurancePolicy> getPolicyById(int policyId);
    public ApiResponse<InsurancePolicy> deletePolicy(int policyId);
    public ApiResponse<List<InsurancePolicy>> displayAll();
}
