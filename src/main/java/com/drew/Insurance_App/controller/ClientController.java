package com.drew.Insurance_App.controller;

import com.drew.Insurance_App.dto.ApiResponse;
import com.drew.Insurance_App.dto.Claim;
import com.drew.Insurance_App.dto.Client;
import com.drew.Insurance_App.dto.ClientClaimSummary;
import com.drew.Insurance_App.service.ClaimService;
import com.drew.Insurance_App.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/createClient/{policyId}")
    public ResponseEntity<ApiResponse<Client>> createClient(@Valid @RequestBody Client client, @PathVariable int policyId) {
        ApiResponse<Client> response = clientService.insertClient(client, policyId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/getClient/{clientId}")
    public ResponseEntity<ApiResponse<Client>> getClientById(@PathVariable int clientId) {
        ApiResponse<Client> response = clientService.getClientById(clientId);
        if (response.getStatus() == 404) {
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateClient/{clientId}")
    public ResponseEntity<ApiResponse<Client>> updateClient(@Valid @RequestBody Client client) {
        ApiResponse<Client> response = clientService.updateClient(client);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/deleteClient/{clientId}")
    public ResponseEntity<ApiResponse<Client>> deleteClientById(@PathVariable int clientId) {
        ApiResponse<Client> response = clientService.deleteClient(clientId);
        if (response.getStatus() == 404) {
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/displayAllClients")
    public ApiResponse<List<Client>> displayAllClients() {
        return clientService.displayAll();
    }

    @GetMapping("/clientsByPolicy/{policyId}")
    public ApiResponse<List<Client>> getClientsByPolicyId(@PathVariable int policyId) {
        return clientService.getClientsByPolicyId(policyId);
    }

    @GetMapping("/claimCountPerClient")
    public ApiResponse<List<ClientClaimSummary>> getClaimCountPerClient() {
        return clientService.getClaimCountPerClient();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; "));
        response.setMessage(sb.toString());
        response.setData(null);
        return ResponseEntity.badRequest().body(response);
    }
}
