package com.example.testtask.service;

import com.example.testtask.entity.ClientEntity;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface ClientService {

    ResponseEntity<?> getClients(String sort, String filter, String filterValue);

    ResponseEntity<?> createClient(ClientEntity clientEntity);

    ResponseEntity<String> updateClient(Long id, ClientEntity clientEntity);

    ResponseEntity<String> deleteClient(Long id);

    Optional<ClientEntity> findById(Long id);
}
