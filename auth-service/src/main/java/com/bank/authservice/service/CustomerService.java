package com.bank.authservice.service;

import com.bank.authservice.dto.LoginRequest;
import com.bank.authservice.dto.LoginResponse;
import com.bank.authservice.entity.Customer;
import com.bank.authservice.entity.Transaction;
import com.bank.authservice.dto.CustomerResponse;
import com.bank.authservice.dto.CreateAccountRequest;
import com.bank.authservice.dto.AccountResponse;

import java.util.List;

public interface CustomerService {

    Customer register(Customer customer);

    LoginResponse login(LoginRequest request);

    String getBalance(String email);

    String deposit(String email, double amount);

    String withdraw(String email, double amount);

    List<Transaction> getTransactions(String email);

    String transfer(String senderEmail, String receiverEmail, double amount);

    CustomerResponse searchByEmail(String email);

    CustomerResponse searchByMobile(String mobile);

    AccountResponse createAccount(String email, CreateAccountRequest request);

    AccountResponse getAccount(String email);
}