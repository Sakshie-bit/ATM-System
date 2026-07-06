package com.bank.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {

    private String accountType;

    private String branch;

    private String ifsc;
}