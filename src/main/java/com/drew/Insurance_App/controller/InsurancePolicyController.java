package com.drew.Insurance_App.controller;

import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.InsurancePolicy;
import com.drew.Insurance_App.service.InsurancePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InsurancePolicyController {

    @Autowired
    private InsurancePolicyService insurancePolicyService;

    @PostMapping("/saveInsurancePolicy")
    public ApiResponse<InsurancePolicy> saveInsurancePolicy(@RequestBody InsurancePolicy insurancePolicy) {
        return insurancePolicyService.insertPolicy(insurancePolicy);
    }

    @GetMapping("/getInsurancePolicy/{insurancePolicyId}")
    public ApiResponse<InsurancePolicy> saveInsurancePolicy(@PathVariable int insurancePolicyId) {
        return insurancePolicyService.getPolicyById(insurancePolicyId);
    }

    @DeleteMapping("/deleteInsurancePolicy/{insurancePolicyId}")
    public ApiResponse<InsurancePolicy> deleteInsurancePolicy(@PathVariable int insurancePolicyId) {
        return insurancePolicyService.deletePolicy(insurancePolicyId);
    }

    @GetMapping("/displayAllPolicy")
    public ApiResponse<List<InsurancePolicy>> displayPolicyList() {
        return insurancePolicyService.displayAll();
    }
}
