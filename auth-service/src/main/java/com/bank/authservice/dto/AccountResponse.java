package com.bank.authservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private String accountNumber;

    private String accountType;

    private Double balance;

    private String branch;

    private String ifsc;

    private String customerName;
}