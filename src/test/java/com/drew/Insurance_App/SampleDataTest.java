package com.drew.Insurance_App;

import com.drew.Insurance_App.dto.Client;
import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.dto.InsurancePolicy;
import com.drew.Insurance_App.repository.ClientRepository;
import com.drew.Insurance_App.repository.ClaimRepository;
import com.drew.Insurance_App.repository.InsurancePolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SampleDataTest {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private InsurancePolicyRepository policyRepository;

    @BeforeEach
    void setup() {
        claimRepository.deleteAll();
        clientRepository.deleteAll();
        policyRepository.deleteAll();
        InsurancePolicy policy = new InsurancePolicy();
        policy.setInsurancePolicyId(100);
        policy.setInsurancePolicyNumber("POL100");
        policy.setInsurancePolicyType("Home");
        policy.setInsurancePolicyCoverageAmount(50000);
        policy.setInsurancePolicyPremium("1000");
        policy.setInsurancePolicyStartDate("2024-01-01");
        policy.setInsurancePolicyEndDate("2025-01-01");
        policyRepository.save(policy);
        Client client = new Client();
        client.setClientId(100);
        client.setClientName("Sample User");
        client.setClientDateOfBirth("1970-01-01");
        client.setClientAddress("100 Sample Blvd");
        client.setClientContactInformation(1112223333L);
        client.setInsurancePolicy(policy);
        clientRepository.save(client);
        Claim claim = new Claim();
        claim.setClaimId(100);
        claim.setClaimNumber("CLM100");
        claim.setClaimDescription("Sample claim");
        claim.setClaimDate(new Date());
        claim.setClaimStatus("OPEN");
        claim.setClaimAmount(5000);
        claim.setInsurancePolicy(policy);
        claimRepository.save(claim);
    }

    @Test
    void testSampleDataLoaded() {
        List<InsurancePolicy> policies = policyRepository.findAll();
        List<Client> clients = clientRepository.findAll();
        List<Claim> claims = claimRepository.findAll();
        assertThat(policies).hasSize(1);
        assertThat(clients).hasSize(1);
        assertThat(claims).hasSize(1);
        assertThat(claims.get(0).getInsurancePolicy().getInsurancePolicyId()).isEqualTo(100);
        assertThat(clients.get(0).getInsurancePolicy().getInsurancePolicyId()).isEqualTo(100);
    }
} 