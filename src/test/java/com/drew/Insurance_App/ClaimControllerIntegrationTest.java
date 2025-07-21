package com.drew.Insurance_App;

import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.dto.InsurancePolicy;
import com.drew.Insurance_App.repository.ClaimRepository;
import com.drew.Insurance_App.repository.InsurancePolicyRepository;
import com.drew.Insurance_App.repository.ClaimAuditLogRepository;
import com.drew.Insurance_App.dto.ClaimAuditLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class ClaimControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private InsurancePolicyRepository policyRepository;
    @Autowired
    private com.drew.Insurance_App.repository.ClientRepository clientRepository;
    @Autowired
    private ClaimAuditLogRepository auditLogRepository;

    private InsurancePolicy policy;
    private Claim claim1;
    private Claim claim2;

    @BeforeEach
    void setup() {
        claimRepository.deleteAll();
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
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fixedDate = sdf.parse("2024-07-21");
            claim1 = new Claim();
            claim1.setClaimId(1);
            claim1.setClaimNumber("CLM001");
            claim1.setClaimDescription("Accident");
            claim1.setClaimDate(fixedDate);
            claim1.setClaimStatus("OPEN");
            claim1.setClaimAmount(100);
            claim1.setInsurancePolicy(policy);
            claim2 = new Claim();
            claim2.setClaimId(2);
            claim2.setClaimNumber("CLM002");
            claim2.setClaimDescription("Fire");
            claim2.setClaimDate(fixedDate);
            claim2.setClaimStatus("CLOSED");
            claim2.setClaimAmount(200);
            claim2.setInsurancePolicy(policy);
            claimRepository.save(claim1);
            claimRepository.save(claim2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetClaimsByPolicyId() throws Exception {
        mockMvc.perform(get("/api/claimsByPolicy/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void testGetClaimsByStatus() throws Exception {
        mockMvc.perform(get("/api/claimsByStatus/OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].claimStatus", is("OPEN")));
    }

    @Test
    void testCreateAndGetClaim() throws Exception {
        String claimJson = "{" +
                "\"claimId\":3," +
                "\"claimNumber\":\"CLM003\"," +
                "\"claimDescription\":\"Test\"," +
                "\"claimDate\":\"2024-07-21T00:00:00.000+00:00\"," +
                "\"claimStatus\":\"OPEN\"," +
                "\"claimAmount\":1500" +
                "}";
        mockMvc.perform(post("/api/createClaim/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(claimJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.claimNumber", is("CLM003")));
        mockMvc.perform(get("/api/getClaim/3"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.claimNumber", is("CLM003")));
    }

    @Test
    void testUpdateAndDeleteClaim() throws Exception {
        String updateJson = "{" +
                "\"claimId\":1," +
                "\"claimNumber\":\"CLM001-UPDATED\"," +
                "\"claimDescription\":\"Updated\"," +
                "\"claimDate\":\"2024-07-21T00:00:00.000+00:00\"," +
                "\"claimStatus\":\"CLOSED\"," +
                "\"claimAmount\":1200" +
                "}";
        mockMvc.perform(put("/api/updateClaim/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.claimNumber", is("CLM001-UPDATED")));
        mockMvc.perform(delete("/api/deleteClaim/1"))
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/api/getClaim/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCreateClaimValidationErrors() throws Exception {
        String invalidJson = "{" +
                "\"claimId\":4," +
                "\"claimAmount\":-100" + // negative amount
                "}";
        mockMvc.perform(post("/api/createClaim/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("claimNumber")))
                .andExpect(jsonPath("$.message", containsString("claimDescription")))
                .andExpect(jsonPath("$.message", containsString("claimDate")))
                .andExpect(jsonPath("$.message", containsString("claimStatus")))
                .andExpect(jsonPath("$.message", containsString("Claim amount must be non-negative")));
    }

    @Test
    void testUpdateClaimValidationErrors() throws Exception {
        String invalidJson = "{" +
                "\"claimId\":1," +
                "\"claimAmount\":-50" +
                "}";
        mockMvc.perform(put("/api/updateClaim/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("claimNumber")))
                .andExpect(jsonPath("$.message", containsString("claimDescription")))
                .andExpect(jsonPath("$.message", containsString("claimDate")))
                .andExpect(jsonPath("$.message", containsString("claimStatus")))
                .andExpect(jsonPath("$.message", containsString("Claim amount must be non-negative")));
    }

    @Test
    void testBulkCreateClaimsAllValid() throws Exception {
        String json = "[" +
                "{" +
                "\"claimId\":10," +
                "\"claimNumber\":\"BULK1\"," +
                "\"claimDescription\":\"Bulk claim 1\"," +
                "\"claimDate\":\"2024-07-21T00:00:00.000+00:00\"," +
                "\"claimStatus\":\"OPEN\"," +
                "\"claimAmount\":100" +
                "}," +
                "{" +
                "\"claimId\":11," +
                "\"claimNumber\":\"BULK2\"," +
                "\"claimDescription\":\"Bulk claim 2\"," +
                "\"claimDate\":\"2024-07-21T00:00:00.000+00:00\"," +
                "\"claimStatus\":\"OPEN\"," +
                "\"claimAmount\":200" +
                "}" +
                "]";
        mockMvc.perform(post("/api/bulkCreateClaims/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", is(202)))
                .andExpect(jsonPath("$[1].status", is(202)));
    }

    @Test
    void testBulkCreateClaimsSomeInvalid() throws Exception {
        String json = "[" +
                "{" +
                "\"claimId\":12," +
                "\"claimNumber\":\"BULK3\"," +
                "\"claimDescription\":\"Bulk claim 3\"," +
                "\"claimDate\":\"2024-07-21T00:00:00.000+00:00\"," +
                "\"claimStatus\":\"OPEN\"," +
                "\"claimAmount\":300" +
                "}," +
                "{" +
                "\"claimId\":13," +
                "\"claimAmount\":-1" + // invalid
                "}" +
                "]";
        mockMvc.perform(post("/api/bulkCreateClaims/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", is(202)))
                .andExpect(jsonPath("$[1].status", is(406)));
    }

    @Test
    void testBulkCreateClaimsAllInvalid() throws Exception {
        String json = "[" +
                "{" +
                "\"claimId\":14," +
                "\"claimAmount\":-10" +
                "}," +
                "{" +
                "\"claimId\":15," +
                "\"claimAmount\":-20" +
                "}" +
                "]";
        mockMvc.perform(post("/api/bulkCreateClaims/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", is(406)))
                .andExpect(jsonPath("$[1].status", is(406)));
    }

    @Test
    void testFilterClaimsByDateRange() throws Exception {
        mockMvc.perform(get("/api/claims/filter")
                .param("startDate", "2024-07-20")
                .param("endDate", "2024-07-22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testFilterClaimsByAmountRange() throws Exception {
        mockMvc.perform(get("/api/claims/filter")
                .param("minAmount", "100")
                .param("maxAmount", "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testFilterClaimsByStatus() throws Exception {
        mockMvc.perform(get("/api/claims/filter")
                .param("status", "OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void testFilterClaimsByAll() throws Exception {
        mockMvc.perform(get("/api/claims/filter")
                .param("startDate", "2024-07-20")
                .param("endDate", "2024-07-22")
                .param("minAmount", "100")
                .param("maxAmount", "200")
                .param("status", "OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void testFilterClaimsNoResults() throws Exception {
        mockMvc.perform(get("/api/claims/filter")
                .param("startDate", "2020-01-01")
                .param("endDate", "2020-01-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void testFilterClaimsInvalidInput() throws Exception {
        mockMvc.perform(get("/api/claims/filter"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("At least one filter parameter is required"));
    }

    @Test
    void testSoftDeleteAndRestoreClaim() throws Exception {
        // Soft delete claim1
        mockMvc.perform(delete("/api/deleteClaim/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("soft deleted")));
        // Should not be returned in queries
        mockMvc.perform(get("/api/getClaim/1"))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(get("/api/claimsByStatus/OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
        // Restore claim1
        mockMvc.perform(post("/api/restoreClaim/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("restored")));
        // Should be returned in queries again
        mockMvc.perform(get("/api/getClaim/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.claimId", is(1)));
        mockMvc.perform(get("/api/claimsByStatus/OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].claimId", is(1)));
    }

    @Test
    void testClaimAuditLogging() throws Exception {
        auditLogRepository.deleteAll();
        // Create
        String claimJson = "{" +
                "\"claimId\":20," +
                "\"claimNumber\":\"AUDIT1\"," +
                "\"claimDescription\":\"Audit create\"," +
                "\"claimDate\":\"2024-07-21T00:00:00.000+00:00\"," +
                "\"claimStatus\":\"OPEN\"," +
                "\"claimAmount\":100" +
                "}";
        mockMvc.perform(post("/api/createClaim/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(claimJson))
                .andExpect(status().is2xxSuccessful());
        // Update
        String updateJson = "{" +
                "\"claimId\":20," +
                "\"claimNumber\":\"AUDIT1-UPDATED\"," +
                "\"claimDescription\":\"Audit update\"," +
                "\"claimDate\":\"2024-07-21T00:00:00.000+00:00\"," +
                "\"claimStatus\":\"CLOSED\"," +
                "\"claimAmount\":200" +
                "}";
        mockMvc.perform(put("/api/updateClaim/20")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().is2xxSuccessful());
        // Delete
        mockMvc.perform(delete("/api/deleteClaim/20"))
                .andExpect(status().isOk());
        // Restore
        mockMvc.perform(post("/api/restoreClaim/20"))
                .andExpect(status().isOk());
        // Verify logs
        java.util.List<ClaimAuditLog> logs = auditLogRepository.findAll();
        assertThat(logs).hasSize(4);
        assertThat(logs.stream().anyMatch(l -> l.getAction().equals("CREATE"))).isTrue();
        assertThat(logs.stream().anyMatch(l -> l.getAction().equals("UPDATE"))).isTrue();
        assertThat(logs.stream().anyMatch(l -> l.getAction().equals("DELETE"))).isTrue();
        assertThat(logs.stream().anyMatch(l -> l.getAction().equals("RESTORE"))).isTrue();
    }
} 