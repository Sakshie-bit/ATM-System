package com.bank.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {

    private String receiverEmail;

    private double amount;
}
