package com.bank.authservice.service.impl;

import com.bank.authservice.dto.LoginRequest;
import com.bank.authservice.dto.LoginResponse;
import com.bank.authservice.entity.Customer;
import com.bank.authservice.entity.Transaction;
import com.bank.authservice.repository.BankAccountRepository;
import com.bank.authservice.repository.CustomerRepository;
import com.bank.authservice.repository.TransactionRepository;
import com.bank.authservice.security.JwtUtil;
import com.bank.authservice.service.CustomerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.bank.authservice.dto.CustomerResponse;
import com.bank.authservice.entity.BankAccount;
import com.bank.authservice.dto.CreateAccountRequest;
import com.bank.authservice.dto.AccountResponse;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionRepository transactionRepository;
    private final JwtUtil jwtUtil;
    private final BankAccountRepository bankAccountRepository;

    public CustomerServiceImpl(CustomerRepository repository,
                               PasswordEncoder passwordEncoder,
                               TransactionRepository transactionRepository,
                               JwtUtil jwtUtil,
                               BankAccountRepository bankAccountRepository) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.transactionRepository = transactionRepository;
        this.jwtUtil = jwtUtil;
        this.bankAccountRepository = bankAccountRepository;
    }

    // ---------------- REGISTER ----------------
    @Override
    public Customer register(Customer customer) {

        if (repository.findByEmail(customer.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        if (repository.findByMobile(customer.getMobile()).isPresent()) {
            throw new RuntimeException("Mobile number already registered");
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        return repository.save(customer);
    }

    // ---------------- LOGIN ----------------
    @Override
    public LoginResponse login(LoginRequest request) {

        Customer customer = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        String token = jwtUtil.generateToken(customer.getEmail());

        return new LoginResponse("Login Successful", token);
    }
    // ---------------- BALANCE ----------------
    @Override
    public String getBalance(String email) {

        BankAccount account = bankAccountRepository.findByCustomerEmail(email)
                .orElseThrow(() -> new RuntimeException("Bank account not found"));

        return "Balance: " + account.getBalance();
    }
    // ---------------- DEPOSIT ----------------
    @Override
    public String deposit(String email, double amount) {

        if (amount <= 0) {
            return "Invalid deposit amount";
        }

        BankAccount account = bankAccountRepository.findByCustomerEmail(email)
                .orElseThrow(() -> new RuntimeException("Bank account not found"));

        account.setBalance(account.getBalance() + amount);
        bankAccountRepository.save(account);

        Transaction tx = Transaction.builder()
                .email(email)
                .type("DEPOSIT")
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(tx);

        return "Deposited: " + amount + " | New Balance: " + account.getBalance();
    }
    // ---------------- WITHDRAW ----------------
    @Override
    public String withdraw(String email, double amount) {

        if (amount <= 0) {
            return "Invalid withdraw amount";
        }

        BankAccount account = bankAccountRepository.findByCustomerEmail(email)
                .orElseThrow(() -> new RuntimeException("Bank account not found"));

        if (account.getBalance() < amount) {
            return "Insufficient Balance";
        }

        account.setBalance(account.getBalance() - amount);
        bankAccountRepository.save(account);

        Transaction tx = Transaction.builder()
                .email(email)
                .type("WITHDRAW")
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(tx);

        return "Withdrawn: " + amount + " | New Balance: " + account.getBalance();
    }
    @Override
    @Transactional
    public String transfer(String senderEmail, String receiverEmail, double amount) {

        if (amount <= 0) {
            return "Invalid transfer amount";
        }

        BankAccount senderAccount = bankAccountRepository.findByCustomerEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        BankAccount receiverAccount = bankAccountRepository.findByCustomerEmail(receiverEmail)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (senderAccount.getBalance() < amount) {
            return "Insufficient Balance";
        }

        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);

        bankAccountRepository.save(senderAccount);
        bankAccountRepository.save(receiverAccount);

        Transaction debit = Transaction.builder()
                .email(senderEmail)
                .type("TRANSFER_DEBIT")
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(debit);

        Transaction credit = Transaction.builder()
                .email(receiverEmail)
                .type("TRANSFER_CREDIT")
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(credit);

        return "Transferred ₹" + amount + " to " + receiverEmail;
    }
    @Override
    public List<Transaction> getTransactions(String email) {
        return transactionRepository.findByEmailOrderByTimestampDesc(email);
    }
    @Override
    public CustomerResponse searchByEmail(String email) {

        Customer customer = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        BankAccount account = bankAccountRepository
                .findByCustomerEmail(customer.getEmail())
                .orElse(null);

        return CustomerResponse.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .mobile(customer.getMobile())
                .balance(account != null ? account.getBalance() : 0.0)
                .build();
    }
    @Override
    public CustomerResponse searchByMobile(String mobile) {

        Customer customer = repository.findByMobile(mobile)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        BankAccount account = bankAccountRepository
                .findByCustomerEmail(customer.getEmail())
                .orElse(null);

        return CustomerResponse.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .mobile(customer.getMobile())
                .balance(account != null ? account.getBalance() : 0.0)
                .build();
    }
    @Override
    public AccountResponse createAccount(String email, CreateAccountRequest request) {

        Customer customer = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (bankAccountRepository.findByCustomerEmail(email).isPresent()) {
            throw new RuntimeException("Customer already has a bank account");
        }

        BankAccount account = BankAccount.builder()
                .accountNumber(UUID.randomUUID().toString().replace("-", "").substring(0, 10))
                .accountType(request.getAccountType())
                .branch(request.getBranch())
                .ifsc(request.getIfsc())
                .balance(0.0)
                .customer(customer)
                .build();

        bankAccountRepository.save(account);

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .branch(account.getBranch())
                .ifsc(account.getIfsc())
                .customerName(customer.getFirstName() + " " + customer.getLastName())
                .build();
    }

    @Override
    public AccountResponse getAccount(String email) {

        BankAccount account = bankAccountRepository.findByCustomerEmail(email)
                .orElseThrow(() -> new RuntimeException("Bank account not found"));

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .branch(account.getBranch())
                .ifsc(account.getIfsc())
                .customerName(account.getCustomer().getFirstName() + " " +
                        account.getCustomer().getLastName())
                .build();
    }
}