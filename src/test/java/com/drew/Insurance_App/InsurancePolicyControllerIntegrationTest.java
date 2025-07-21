package com.drew.Insurance_App;

import com.drew.Insurance_App.dto.InsurancePolicy;
import com.drew.Insurance_App.repository.InsurancePolicyRepository;
import com.drew.Insurance_App.repository.ClaimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InsurancePolicyControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InsurancePolicyRepository policyRepository;
    @Autowired
    private ClaimRepository claimRepository;

    @BeforeEach
    void setup() {
        claimRepository.deleteAll();
        policyRepository.deleteAll();
    }

    @Test
    void testSaveInsurancePolicyValidationErrors() throws Exception {
        String invalidJson = "{" +
                "\"insurancePolicyId\":10," +
                "\"insurancePolicyCoverageAmount\":0" + // not positive
                "}";
        mockMvc.perform(post("/api/saveInsurancePolicy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("insurancePolicyNumber")))
                .andExpect(jsonPath("$.message", containsString("insurancePolicyType")))
                .andExpect(jsonPath("$.message", containsString("insurancePolicyPremium")))
                .andExpect(jsonPath("$.message", containsString("insurancePolicyStartDate")))
                .andExpect(jsonPath("$.message", containsString("insurancePolicyEndDate")))
                .andExpect(jsonPath("$.message", containsString("Coverage amount must be positive")));
    }
} 