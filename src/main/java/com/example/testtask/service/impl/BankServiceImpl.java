package com.example.testtask.service.impl;

import com.example.testtask.entity.BankEntity;
import com.example.testtask.entity.ClientEntity;
import com.example.testtask.model.BankModel;
import com.example.testtask.repository.BankRepository;
import com.example.testtask.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Primary
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

    @Autowired
    BankServiceImpl(BankRepository bankRepository){
        this.bankRepository = bankRepository;
    }

    public List<BankEntity> getBanksFromDB() {
        return (List<BankEntity>) bankRepository.findAll();
    }

    public void saveBank(BankEntity bank) {
        bankRepository.save(bank);
    }

    @Override
    public ResponseEntity<?> getBanks(String sort, String filter, String filterValue) {
        try {
            Stream<BankModel> bankStream = getBanksFromDB()
                    .stream()
                    .map(BankModel::toModel);

            if (filter != null && !filter.isEmpty() && filterValue != null) {
                bankStream = bankStream.filter(bank -> {
                    switch (filter) {
                        case "name":
                            return filterValue.equals(bank.getName());
                        case "bankIdentificationCode":
                            return filterValue.equals(Long.toString(bank.getBankIdentificationCode()));
                        default:
                            return true;
                    }
                });
            }

            List<BankModel> banks = bankStream.collect(Collectors.toList());

            if (sort != null && !sort.isEmpty()) {
                Comparator<BankModel> comparator = getComparator(sort);
                if (comparator != null) {
                    banks.sort(comparator);
                }
            }
            return ResponseEntity.ok(banks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    String.format("Something went wrong \n %s", e.getMessage())
            );
        }
    }

    private Comparator<BankModel> getComparator(String sort) {
        switch (sort) {
            case "name":
                return Comparator.comparing(BankModel::getName);
            case "bankIdentificationCode":
                return Comparator.comparing(BankModel::getBankIdentificationCode);
            default:
                return null;
        }
    }

    @Override
    public Optional<BankEntity> findById(Long id) {
        return bankRepository.findById(id);
    }

    @Override
    public ResponseEntity<?> createBank(BankEntity bankEntity) {
        try {
            saveBank(bankEntity);
            return ResponseEntity.ok("Bank created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    String.format("Something went wrong \n %s", e.getMessage())
            );
        }
    }

    @Override
    public ResponseEntity<String> updateBank(Long id, BankEntity bankEntity) {
        try {
            return bankRepository.findById(id)
                    .map(existingBank -> {
                        existingBank.setName(bankEntity.getName());
                        existingBank.setBankIdentificationCode(bankEntity.getBankIdentificationCode());
                        saveBank(existingBank);
                        return ResponseEntity.ok("Bank updated successfully");
                    })
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    String.format("Something went wrong \n %s", e.getMessage())
            );
        }
    }

    @Override
    public ResponseEntity<String> deleteBank(Long id) {
        return bankRepository.findById(id)
                .map(bank -> {
                    bankRepository.delete(bank);
                    return ResponseEntity.ok("Bank deleted successfully");
                }).orElse(ResponseEntity.notFound().build());
    }
}
