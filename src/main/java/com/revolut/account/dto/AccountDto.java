package com.revolut.account.dto;

import com.revolut.account.entity.Account;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    private final int id;
    private final String name;
    private final BigDecimal balance;

    public AccountDto(int id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public static AccountDto buildAccount(Account account) {
        return new AccountDto(account.id, account.name, account.balance.get());
    }
}
