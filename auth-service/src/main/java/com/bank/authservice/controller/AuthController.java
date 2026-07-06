package com.bank.authservice.controller;

import com.bank.authservice.dto.LoginRequest;
import com.bank.authservice.dto.LoginResponse;
import com.bank.authservice.entity.Customer;
import com.bank.authservice.service.CustomerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CustomerService customerService;

    public AuthController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public Customer register(@RequestBody Customer customer) {
        return customerService.register(customer);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return customerService.login(request);
    }
}