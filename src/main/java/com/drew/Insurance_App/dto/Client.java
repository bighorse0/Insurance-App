package com.drew.Insurance_App.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Entity
@Getter
@Setter
public class Client {
    @Id
    @Column(name = "clientId")
    private int clientId;

    @NotBlank(message = "Client name is required")
    private String clientName;

    @NotBlank(message = "Client date of birth is required")
    private String clientDateOfBirth;

    @NotBlank(message = "Client address is required")
    private String clientAddress;

    @NotNull(message = "Client contact information is required")
    @Min(value = 1000000000L, message = "Contact info must be a 10-digit number")
    @Max(value = 9999999999L, message = "Contact info must be a 10-digit number")
    private long clientContactInformation;

    @ManyToOne
    @JoinColumn(name = "policyId")
    private InsurancePolicy insurancePolicy;
}
