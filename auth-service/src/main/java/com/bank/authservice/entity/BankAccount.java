package com.bank.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountType;

    @Column(nullable = false)
    private Double balance = 0.0;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private String ifsc;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}