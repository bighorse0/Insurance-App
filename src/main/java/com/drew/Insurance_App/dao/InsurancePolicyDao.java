package com.drew.Insurance_App.dao;

import com.drew.Insurance_App.dto.Client;
import com.drew.Insurance_App.dto.InsurancePolicy;
import com.drew.Insurance_App.repository.ClientRepository;
import com.drew.Insurance_App.repository.InsurancePolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InsurancePolicyDao {
    @Autowired
    private InsurancePolicyRepository insurancePolicyRepository;
    // CREATE
    public InsurancePolicy insertPolicy(InsurancePolicy policy) {
        return insurancePolicyRepository.save(policy);
    }

    // READ
    public InsurancePolicy getByPolicyId(int policyId) {
        Optional<InsurancePolicy> policy = insurancePolicyRepository.findById(policyId);

        return policy.orElse(null);
    }

    // UPDATE
    // Should we be able to update PolicyNumber/PolicyId for instance? Probably not. For now, will not include UPDATE even if certain aspects of the policy may be updated.

    // DELETE
    public InsurancePolicy deletePolicyById(int policyId) {
        Optional<InsurancePolicy> policy = insurancePolicyRepository.findById(policyId);

        if (policy.isPresent()) {
            insurancePolicyRepository.deleteById(policyId); // can use policy.get() instead of policyId
            return policy.get();
        }

        return null;
    }

    public List<InsurancePolicy> displayAll() {
        return insurancePolicyRepository.findAll();
    }

}
