package com.drew.Insurance_App.repository;

import com.drew.Insurance_App.dto.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Integer> {
}
