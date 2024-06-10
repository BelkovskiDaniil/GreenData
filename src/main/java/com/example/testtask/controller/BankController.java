package com.example.testtask.controller;

import com.example.testtask.entity.BankEntity;
import com.example.testtask.service.BankService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/bank")
public class BankController {

    private final BankService bankService;

    @Autowired
    BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public ResponseEntity<?> getBanks(@RequestParam(name = "sort_by", required = false) String sort,
                                      @RequestParam(name = "filter_by", required = false) String filter,
                                      @RequestParam(name = "filter_value", required = false) String filterValue) {
        return bankService.getBanks(sort, filter, filterValue);
    }

    @PostMapping
    public ResponseEntity<?> createBank(@Valid @RequestBody BankEntity bankEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }
        return bankService.createBank(bankEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBank(@PathVariable("id") Long id, @Valid @RequestBody BankEntity bankEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }
        return bankService.updateBank(id, bankEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBank(@PathVariable("id") long id) {
        return bankService.deleteBank(id);
    }
}
