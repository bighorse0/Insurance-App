package com.drew.Insurance_App.dao;

import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.repository.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClaimDao {
    @Autowired
    private ClaimRepository claimRepository;
    // CREATE
    public Claim insertClaim(Claim claim) {
        return claimRepository.save(claim);
    }

    // READ
    public Claim getByClaimId(int claimId) {
        Optional<Claim> claim = claimRepository.findById(claimId);

        return claim.orElse(null);
    }

    // UPDATE
    public Claim updateClaim(Claim claim) {
        return claimRepository.save(claim);
    }

    // DELETE
    public Claim deleteClaimById(int claimId) {
        Optional<Claim> claim = claimRepository.findById(claimId);

        if (claim.isPresent()) {
            claimRepository.deleteById(claimId);
            return claim.get();
        }

        return null;
    }

    public List<Claim> displayAll() {
        return claimRepository.findAll();
    }
}
