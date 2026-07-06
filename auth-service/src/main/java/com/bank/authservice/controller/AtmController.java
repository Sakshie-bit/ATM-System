package com.bank.authservice.controller;

import com.bank.authservice.entity.Transaction;
import com.bank.authservice.service.CustomerService;
import org.springframework.web.bind.annotation.*;
import com.bank.authservice.security.JwtUtil;
import java.util.List;
import com.bank.authservice.dto.TransferRequest;
import com.bank.authservice.dto.CustomerResponse;
import com.bank.authservice.dto.CreateAccountRequest;
import com.bank.authservice.dto.AccountResponse;

@RestController
@RequestMapping("/atm")
public class AtmController {

    private final CustomerService customerService;
    private final JwtUtil jwtUtil;

    public AtmController(CustomerService customerService,
                         JwtUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/balance")
    public String balance(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractEmail(token);

        return customerService.getBalance(email);
    }

    @PostMapping("/deposit")
    public String deposit(@RequestHeader("Authorization") String authHeader,
                          @RequestParam double amount) {

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        return customerService.deposit(email, amount);
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestHeader("Authorization") String authHeader,
                           @RequestParam double amount) {

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        return customerService.withdraw(email, amount);
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        return customerService.getTransactions(email);
    }
    @PostMapping("/transfer")
    public String transfer(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TransferRequest request) {

        String token = authHeader.substring(7);
        String senderEmail = jwtUtil.extractEmail(token);

        return customerService.transfer(
                senderEmail,
                request.getReceiverEmail(),
                request.getAmount());
    }
    @GetMapping("/search/email")
    public CustomerResponse searchByEmail(@RequestParam String email) {

        return customerService.searchByEmail(email);
    }
    @GetMapping("/search/mobile")
    public CustomerResponse searchByMobile(@RequestParam String mobile) {

        return customerService.searchByMobile(mobile);
    }
    @PostMapping("/account/create")
    public AccountResponse createAccount(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateAccountRequest request) {

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        return customerService.createAccount(email, request);
    }
    @GetMapping("/account/details")
    public AccountResponse getAccount(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        return customerService.getAccount(email);
    }
}