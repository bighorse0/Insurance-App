package com.drew.Insurance_App.dto;

public class ClientClaimSummary {
    private int clientId;
    private String clientName;
    private int claimCount;

    public ClientClaimSummary(int clientId, String clientName, int claimCount) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.claimCount = claimCount;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getClaimCount() {
        return claimCount;
    }

    public void setClaimCount(int claimCount) {
        this.claimCount = claimCount;
    }
} 