package com.drew.Insurance_App.controller;

import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.dto.Client;
import com.drew.Insurance_App.service.ClaimService;
import com.drew.Insurance_App.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/createClient/{policyId}")
    public ApiResponse<Client> createClient(@RequestBody Client client, @PathVariable int policyId) {
        return clientService.insertClient(client, policyId);
    }

    @GetMapping("/getClient/{clientId}")
    public ApiResponse<Client> getClientById(@PathVariable int clientId) {
        return clientService.getClientById(clientId);
    }

    @PutMapping("/updateClient/{clientId}")
    public ApiResponse<Client> updateClient(@RequestBody Client client) {
        return clientService.updateClient(client);
    }

    @DeleteMapping("/deleteClient/{clientId}")
    public ApiResponse<Client> deleteClientById(@PathVariable int clientId) {
        return clientService.deleteClient(clientId);
    }

    @GetMapping("/displayAllClients")
    public ApiResponse<List<Client>> displayAllClients() {
        return clientService.displayAll();
    }
}
