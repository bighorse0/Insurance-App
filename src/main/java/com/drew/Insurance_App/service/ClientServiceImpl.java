package com.drew.Insurance_App.service;


import com.drew.Insurance_App.dao.ClaimDao;
import com.drew.Insurance_App.dao.ClientDao;
import com.drew.Insurance_App.dao.InsurancePolicyDao;
import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.dto.Client;
import com.drew.Insurance_App.dto.InsurancePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private InsurancePolicyDao insurancePolicyDao;

    @Autowired
    private ApiResponse<Client> clientApiResponse;

    @Autowired
    private ApiResponse<List<Client>> findAllClientsApiResponse;

    @Override
    public ApiResponse<Client> insertClient(Client client, int policyId) {
        InsurancePolicy insurancePolicy = insurancePolicyDao.getByPolicyId(policyId);

        if (Objects.isNull(insurancePolicy)) {
            clientApiResponse.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            clientApiResponse.setMessage("Has not been claimed yet");
            clientApiResponse.setData(null);
        } else {
            client.setInsurancePolicy(insurancePolicy);
            clientDao.insertClient(client);

            clientApiResponse.setStatus(HttpStatus.ACCEPTED.value());
            clientApiResponse.setMessage("Claim successful");
            clientApiResponse.setData(client);
        }

        return clientApiResponse;
    }

    @Override
    public ApiResponse<Client> getClientById(int clientId) {
        Client client = clientDao.getByClientId(clientId);

        if (Objects.isNull(client)) {
            throw new RuntimeException("Claim not found");
        }

        clientApiResponse.setStatus(HttpStatus.ACCEPTED.value());
        clientApiResponse.setMessage("Claim exists");
        clientApiResponse.setData(client);

        return clientApiResponse;
    }

    @Override
    public ApiResponse<Client> updateClient(Client client) {
        Client updatedClient = clientDao.deleteClientById(client.getClientId());

        if (Objects.isNull(updatedClient)) {
            throw new RuntimeException("Client not found");
        }

        updatedClient.setClientName(client.getClientName());
        updatedClient.setClientContactInformation(client.getClientContactInformation());
        updatedClient.setClientDateOfBirth(client.getClientDateOfBirth());
        updatedClient.setClientAddress(client.getClientAddress());

        clientDao.updateClient(updatedClient);

        clientApiResponse.setStatus(HttpStatus.ACCEPTED.value());
        clientApiResponse.setMessage("Client updated successfully");
        clientApiResponse.setData(updatedClient);

        return clientApiResponse;
    }

    @Override
    public ApiResponse<Client> deleteClient(int clientId) {
        Client client = clientDao.getByClientId(clientId);

        if (Objects.isNull(client)) {
            throw new RuntimeException("Client not found");
        }

        clientApiResponse.setStatus(HttpStatus.FOUND.value());
        clientApiResponse.setMessage("Client deleted successfully");
        clientApiResponse.setData(client);

        return clientApiResponse;
    }

    @Override
    public ApiResponse<List<Client>> displayAll() {
        List<Client> clients = clientDao.displayAll();

        if (Objects.isNull(clients)) {
            findAllClientsApiResponse.setStatus(HttpStatus.NOT_FOUND.value());
            findAllClientsApiResponse.setMessage("Clients not found");
            findAllClientsApiResponse.setData(null);
        } else {
            findAllClientsApiResponse.setStatus(HttpStatus.FOUND.value());
            findAllClientsApiResponse.setMessage("All clients found");
            findAllClientsApiResponse.setData(clients);
        }

        return findAllClientsApiResponse;
    }
}
