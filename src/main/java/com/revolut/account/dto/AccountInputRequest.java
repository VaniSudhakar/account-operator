package com.revolut.account.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountInputRequest {
    private int id;
    private String name;
    private BigDecimal amount;
}
