package com.example.testtask.service.impl;

import com.example.testtask.entity.DepositEntity;
import com.example.testtask.model.DepositModel;
import com.example.testtask.repository.BankRepository;
import com.example.testtask.repository.DepositRepository;
import com.example.testtask.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.testtask.repository.ClientRepository;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Primary
public class DepositServiceImpl implements DepositService {

    private final DepositRepository depositRepository;
    private final ClientRepository clientRepository;
    private final BankRepository bankRepository;

    @Autowired
    DepositServiceImpl(DepositRepository depositRepository, ClientRepository clientRepository, BankRepository bankRepository) {
        this.depositRepository = depositRepository;
        this.clientRepository = clientRepository;
        this.bankRepository = bankRepository;
    }

    public List<DepositEntity> getDepositsFromDB() {
        return (List<DepositEntity>) depositRepository.findAll();
    }

    public void saveDeposit(DepositEntity deposit) {
        depositRepository.save(deposit);
    }

    @Override
    public ResponseEntity<?> getDeposits(String sort, String filter, String filterValue) {
        try {
            Stream<DepositModel> depositStream = getDepositsFromDB()
                    .stream()
                    .map(DepositModel::toModel);

            if (filter != null && !filter.isEmpty() && filterValue != null) {
                depositStream = depositStream.filter(deposit -> {
                    switch (filter) {
                        case "percent":
                            return filterValue.equals(String.valueOf(deposit.getPercent()));
                        case "dateOpen":
                            return filterValue.equals(deposit.getDateOpen().toString());
                        case "monthPeriod":
                            return filterValue.equals(String.valueOf(deposit.getMonthPeriod()));
                        default:
                            return true;
                    }
                });
            }

            List<DepositModel> deposits = depositStream.collect(Collectors.toList());

            if (sort != null && !sort.isEmpty()) {
                Comparator<DepositModel> comparator = getComparator(sort);
                if (comparator != null) {
                    deposits.sort(comparator);
                }
            }

            return ResponseEntity.ok(deposits);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    String.format("Something went wrong \n %s", e.getMessage())
            );
        }
    }

    private Comparator<DepositModel> getComparator(String sort) {
        switch (sort) {
            case "percent":
                return Comparator.comparing(DepositModel::getPercent);
            case "dateOpen":
                return Comparator.comparing(DepositModel::getDateOpen);
            case "monthPeriod":
                return Comparator.comparing(DepositModel::getMonthPeriod);
            default:
                return null;
        }
    }

    @Override
    public ResponseEntity<?> createDeposit(DepositEntity depositEntity, Long bankId, Long clientId) {
        try {
            DepositEntity depositEntityNew = new DepositEntity();
            depositEntityNew.setPercent(depositEntity.getPercent());
            depositEntityNew.setDateOpen(depositEntity.getDateOpen());
            depositEntityNew.setMonthPeriod(depositEntity.getMonthPeriod());
            depositEntityNew.setClient(clientRepository.findById(clientId).get());
            depositEntityNew.setBank(bankRepository.findById(bankId).get());
            saveDeposit(depositEntityNew);
            return ResponseEntity.ok("Deposit created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    String.format("Something went wrong \n %s", e.getMessage())
            );
        }
    }

    @Override
    public ResponseEntity<String> updateDeposit(Long id, DepositEntity depositEntity, Long bankId, Long clientId) {
        try {
            return depositRepository.findById(id)
                    .map(existingDeposit -> {
                        existingDeposit.setPercent(depositEntity.getPercent());
                        existingDeposit.setDateOpen(depositEntity.getDateOpen());
                        existingDeposit.setMonthPeriod(depositEntity.getMonthPeriod());
                        existingDeposit.setClient(clientRepository.findById(clientId).get());
                        existingDeposit.setBank(bankRepository.findById(bankId).get());
                        saveDeposit(existingDeposit);
                        return ResponseEntity.ok("Deposit updated successfully");
                    })
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    String.format("Something went wrong \n %s", e.getMessage())
            );
        }
    }

    @Override
    public ResponseEntity<String> deleteDeposit(Long id) {
        return depositRepository.findById(id)
                .map(deposit -> {
                    deposit.setClient(null);
                    deposit.setBank(null);
                    depositRepository.save(deposit);
                    depositRepository.delete(deposit);
                    return ResponseEntity.ok("Deposit deleted successfully!");
                }).orElse(ResponseEntity.notFound().build());
    }

}
