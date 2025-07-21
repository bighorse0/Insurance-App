package com.drew.Insurance_App.controller;

import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.InsurancePolicy;
import com.drew.Insurance_App.service.InsurancePolicyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InsurancePolicyController {

    @Autowired
    private InsurancePolicyService insurancePolicyService;

    @PostMapping("/saveInsurancePolicy")
    public ResponseEntity<ApiResponse<InsurancePolicy>> saveInsurancePolicy(@Valid @RequestBody InsurancePolicy insurancePolicy) {
        ApiResponse<InsurancePolicy> response = insurancePolicyService.insertPolicy(insurancePolicy);
        return ResponseEntity.status(response.getStatus()).body(response);
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
