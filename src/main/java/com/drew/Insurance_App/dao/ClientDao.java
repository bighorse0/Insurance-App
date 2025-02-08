package com.drew.Insurance_App.dao;

import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.dto.Client;
import com.drew.Insurance_App.repository.ClaimRepository;
import com.drew.Insurance_App.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientDao {
    @Autowired
    private ClientRepository clientRepository;
    // CREATE
    public Client insertClient(Client client) {
        return clientRepository.save(client);
    }

    // READ
    public Client getByClientId(int clientId) {
        Optional<Client> client = clientRepository.findById(clientId);

        return client.orElse(null);
    }

    // UPDATE
    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }

    // DELETE
    public Client deleteClientById(int clientId) {
        Optional<Client> client = clientRepository.findById(clientId);

        if (client.isPresent()) {
            clientRepository.deleteById(clientId);
            return client.get();
        }

        return null;
    }

    public List<Client> displayAll() {
        return clientRepository.findAll();
    }
}
