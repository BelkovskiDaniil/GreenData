package com.example.testtask.service;

import com.example.testtask.entity.BankEntity;
import org.springframework.http.ResponseEntity;

public interface BankService {

    ResponseEntity<?> getBanks(String sort, String filter, String filterValue);

    ResponseEntity<?> createBank(BankEntity bankEntity);

    ResponseEntity<String> updateBank(Long id, BankEntity bankEntity);

    ResponseEntity<String> deleteBank(Long id);
}
