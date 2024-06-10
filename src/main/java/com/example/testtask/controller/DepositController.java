package com.example.testtask.controller;

import com.example.testtask.entity.DepositEntity;
import com.example.testtask.service.DepositService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/deposit")
public class DepositController {

    private final DepositService depositService;

    @Autowired
    DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @GetMapping
    public ResponseEntity<?> getDeposits(@RequestParam(name = "sort_by", required = false) String sort,
                                         @RequestParam(name = "filter_by", required = false) String filter,
                                         @RequestParam(name = "filter_value", required = false) String filterValue) {
        return depositService.getDeposits(sort, filter, filterValue);
    }

    @PostMapping
    public ResponseEntity<?> createDeposit(@Valid @RequestBody DepositEntity depositEntity,
                                           @RequestParam(name = "bank_id") Long bankId,
                                           @RequestParam(name = "client_id") Long clientId,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors() || bankId == null || clientId == null || bankId == 0 || clientId == 0) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            if (bankId == null || clientId == null) {
                errorMessages.add("Bank ID и Client ID не должны быть пустыми.");
            }
            if (bankId != null && bankId == 0) {
                errorMessages.add("Bank ID не должен быть нулевым.");
            }
            if (clientId != null && clientId == 0) {
                errorMessages.add("Client ID не должен быть нулевым.");
            }
            return ResponseEntity.badRequest().body(errorMessages);
        }
        return depositService.createDeposit(depositEntity, bankId, clientId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDeposit(@Valid @PathVariable("id") Long id,
                                           @RequestBody DepositEntity depositEntity,
                                           @RequestParam(name = "bank_id") Long bankId,
                                           @RequestParam(name = "client_id") Long clientId,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors() || bankId == null || clientId == null || bankId == 0 || clientId == 0) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            if (bankId == null || clientId == null) {
                errorMessages.add("Bank ID и Client ID не должны быть пустыми.");
            }
            if (bankId != null && bankId == 0) {
                errorMessages.add("Bank ID не должен быть нулевым.");
            }
            if (clientId != null && clientId == 0) {
                errorMessages.add("Client ID не должен быть нулевым.");
            }
            return ResponseEntity.badRequest().body(errorMessages);
        }
        return depositService.updateDeposit(id, depositEntity, bankId, clientId);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDeposit(@PathVariable("id") Long id) {
        return depositService.deleteDeposit(id);
    }
}
