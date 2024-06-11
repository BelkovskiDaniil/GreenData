package com.example.testtask.service;

import com.example.testtask.entity.BankEntity;
import com.example.testtask.entity.ClientEntity;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface BankService {

    ResponseEntity<?> getBanks(String sort, String filter, String filterValue);

    ResponseEntity<?> createBank(BankEntity bankEntity);

    ResponseEntity<String> updateBank(Long id, BankEntity bankEntity);

    ResponseEntity<String> deleteBank(Long id);

    Optional<BankEntity> findById(Long id);
}
