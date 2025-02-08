package com.drew.Insurance_App.repository;

import com.drew.Insurance_App.dto.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Integer> {

}
