package com.drew.Insurance_App.repository;

import com.drew.Insurance_App.dto.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {


}
