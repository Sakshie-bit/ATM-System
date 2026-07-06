package com.bank.authservice.repository;

import com.bank.authservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByEmailOrderByTimestampDesc(String email);
}