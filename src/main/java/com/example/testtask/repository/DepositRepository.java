package com.example.testtask.repository;

import com.example.testtask.entity.DepositEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends CrudRepository<DepositEntity, Long> {
}
