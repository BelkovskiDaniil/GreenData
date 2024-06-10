package com.example.testtask.service;

import com.example.testtask.entity.DepositEntity;
import org.springframework.http.ResponseEntity;

public interface DepositService {

    ResponseEntity<?> getDeposits(String sort, String filter, String filterValue);

    ResponseEntity<?> createDeposit(DepositEntity depositEntity, Long bankId, Long clientId);

    ResponseEntity<String> updateDeposit(Long id, DepositEntity depositEntity, Long bankId, Long clientId);

    ResponseEntity<String> deleteDeposit(Long id);
}
