package com.drew.Insurance_App.service;

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
public class InsurancePolicyServiceImpl implements InsurancePolicyService {

    @Autowired
    private InsurancePolicyDao insurancePolicyDao;

    @Autowired
    private ApiResponse<InsurancePolicy> insurancePolicyApiResponse;

    @Autowired
    private ApiResponse<List<InsurancePolicy>> findAllPolicyApiResponse;

    @Override
    public ApiResponse<InsurancePolicy> insertPolicy(InsurancePolicy insurancePolicy) {
        insurancePolicy = insurancePolicyDao.insertPolicy(insurancePolicy);

        if (Objects.isNull(insurancePolicy)) {
            insurancePolicyApiResponse.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            insurancePolicyApiResponse.setMessage("Policy has not been inserted");
            insurancePolicyApiResponse.setData(null);
        } else {
            insurancePolicyApiResponse.setStatus(HttpStatus.ACCEPTED.value());
            insurancePolicyApiResponse.setMessage("Policy added successful");
            insurancePolicyApiResponse.setData(insurancePolicy);
        }

        return insurancePolicyApiResponse;
    }

    @Override
    public ApiResponse<InsurancePolicy> getPolicyById(int policyId) {
        InsurancePolicy policy = insurancePolicyDao.getByPolicyId(policyId);

        if (Objects.isNull(policy)) {
            throw new RuntimeException("Policy not found");
        }

        insurancePolicyApiResponse.setStatus(HttpStatus.ACCEPTED.value());
        insurancePolicyApiResponse.setMessage("Policy exists");
        insurancePolicyApiResponse.setData(policy);

        return insurancePolicyApiResponse;
    }

    @Override
    public ApiResponse<InsurancePolicy> deletePolicy(int policyId) {
        InsurancePolicy policy = insurancePolicyDao.deletePolicyById(policyId);

        if (Objects.isNull(policy)) {
            throw new RuntimeException("Policy not found");
        }

        insurancePolicyApiResponse.setStatus(HttpStatus.FOUND.value());
        insurancePolicyApiResponse.setMessage("Policy deleted successfully");
        insurancePolicyApiResponse.setData(policy);

        return insurancePolicyApiResponse;
    }

    @Override
    public ApiResponse<List<InsurancePolicy>> displayAll() {
        List<InsurancePolicy> policyList = insurancePolicyDao.displayAll();

        if (Objects.isNull(policyList)) {
            findAllPolicyApiResponse.setStatus(HttpStatus.NOT_FOUND.value());
            findAllPolicyApiResponse.setMessage("Policy list not found");
            findAllPolicyApiResponse.setData(null);
        } else {
            findAllPolicyApiResponse.setStatus(HttpStatus.FOUND.value());
            findAllPolicyApiResponse.setMessage("All policies found");
            findAllPolicyApiResponse.setData(policyList);
        }

        return findAllPolicyApiResponse;
    }
}
