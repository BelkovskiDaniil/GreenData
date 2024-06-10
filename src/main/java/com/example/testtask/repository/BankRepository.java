package com.example.testtask.repository;

import com.example.testtask.entity.BankEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends CrudRepository<BankEntity, Long> {
}
