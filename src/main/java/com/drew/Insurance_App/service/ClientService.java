package com.drew.Insurance_App.service;

import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.dto.Client;
import com.drew.Insurance_App.dto.ClientClaimSummary;

import java.util.List;

public interface ClientService {
    public ApiResponse<Client> insertClient(Client client, int policyId);
    public ApiResponse<Client> getClientById(int claimId);
    public ApiResponse<Client> updateClient(Client client);
    public ApiResponse<Client> deleteClient(int claimId);
    public ApiResponse<List<Client>> displayAll();
    public ApiResponse<List<Client>> getClientsByPolicyId(int policyId);
    public ApiResponse<List<ClientClaimSummary>> getClaimCountPerClient();
}
