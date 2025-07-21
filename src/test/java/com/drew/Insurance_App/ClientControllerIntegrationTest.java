package com.drew.Insurance_App;

import com.drew.Insurance_App.dto.Client;
import com.drew.Insurance_App.dto.InsurancePolicy;
import com.drew.Insurance_App.repository.ClientRepository;
import com.drew.Insurance_App.repository.InsurancePolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private InsurancePolicyRepository policyRepository;

    private InsurancePolicy policy;
    private Client client1;
    private Client client2;

    @BeforeEach
    void setup() {
        // Clean up in order: claims, clients, policies
        // No claimRepository here, so only clients and policies
        clientRepository.deleteAll();
        policyRepository.deleteAll();
        policy = new InsurancePolicy();
        policy.setInsurancePolicyId(1);
        policy.setInsurancePolicyNumber("POL123");
        policy.setInsurancePolicyType("Auto");
        policy.setInsurancePolicyCoverageAmount(10000);
        policy.setInsurancePolicyPremium("500");
        policy.setInsurancePolicyStartDate("2024-01-01");
        policy.setInsurancePolicyEndDate("2025-01-01");
        policyRepository.save(policy);
        client1 = new Client();
        client1.setClientId(1);
        client1.setClientName("Alice");
        client1.setClientDateOfBirth("1990-01-01");
        client1.setClientAddress("123 Main St");
        client1.setClientContactInformation(1234567890L);
        client1.setInsurancePolicy(policy);
        client2 = new Client();
        client2.setClientId(2);
        client2.setClientName("Bob");
        client2.setClientDateOfBirth("1985-05-05");
        client2.setClientAddress("456 Oak Ave");
        client2.setClientContactInformation(9876543210L);
        client2.setInsurancePolicy(policy);
        clientRepository.save(client1);
        clientRepository.save(client2);
    }

    @Test
    void testGetClientsByPolicyId() throws Exception {
        mockMvc.perform(get("/api/clientsByPolicy/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void testCreateAndGetClient() throws Exception {
        String clientJson = "{" +
                "\"clientId\":3," +
                "\"clientName\":\"Charlie\"," +
                "\"clientDateOfBirth\":\"2000-01-01\"," +
                "\"clientAddress\":\"789 Pine Rd\"," +
                "\"clientContactInformation\":5555555555" +
                "}";
        mockMvc.perform(post("/api/createClient/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.clientName", is("Charlie")));
        mockMvc.perform(get("/api/getClient/3"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.clientName", is("Charlie")));
    }

    @Test
    void testCreateClientValidationErrors() throws Exception {
        String invalidJson = "{" +
                "\"clientId\":4," +
                "\"clientContactInformation\":123" + // too short
                "}";
        mockMvc.perform(post("/api/createClient/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("clientName")))
                .andExpect(jsonPath("$.message", containsString("clientDateOfBirth")))
                .andExpect(jsonPath("$.message", containsString("clientAddress")))
                .andExpect(jsonPath("$.message", containsString("Contact info must be a 10-digit number")));
    }

    @Test
    void testUpdateAndDeleteClient() throws Exception {
        String updateJson = "{" +
                "\"clientId\":1," +
                "\"clientName\":\"Alice Updated\"," +
                "\"clientDateOfBirth\":\"1990-01-01\"," +
                "\"clientAddress\":\"123 Main St\"," +
                "\"clientContactInformation\":1234567890" +
                "}";
        mockMvc.perform(put("/api/updateClient/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.clientName", is("Alice Updated")));
        mockMvc.perform(delete("/api/deleteClient/1"))
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/api/getClient/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateClientValidationErrors() throws Exception {
        String invalidJson = "{" +
                "\"clientId\":1," +
                "\"clientContactInformation\":99999999999" + // too long
                "}";
        mockMvc.perform(put("/api/updateClient/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("clientName")))
                .andExpect(jsonPath("$.message", containsString("clientDateOfBirth")))
                .andExpect(jsonPath("$.message", containsString("clientAddress")))
                .andExpect(jsonPath("$.message", containsString("Contact info must be a 10-digit number")));
    }

    @Test
    void testClaimCountPerClient() throws Exception {
        mockMvc.perform(get("/api/claimCountPerClient"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].clientName", anyOf(is("Alice"), is("Bob"))));
    }
} 