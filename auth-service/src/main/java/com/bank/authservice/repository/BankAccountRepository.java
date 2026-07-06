package com.bank.authservice.repository;

import com.bank.authservice.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByAccountNumber(String accountNumber);

    Optional<BankAccount> findByCustomerEmail(String email);
}