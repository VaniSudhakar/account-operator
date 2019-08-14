package com.revolut.account.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountTransferRequest {

    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;

}