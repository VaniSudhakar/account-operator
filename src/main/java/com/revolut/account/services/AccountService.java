package com.revolut.account.services;

import com.revolut.account.dto.AccountDto;
import com.revolut.account.dto.AccountInputRequest;
import com.revolut.account.entity.Account;
import com.revolut.account.exception.AccountCreationException;
import com.revolut.account.exception.LowBalanceException;
import com.revolut.account.exception.TransferException;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    List<AccountDto> getAllAccounts();
    AccountDto createAccount(AccountInputRequest AccountInputRequest) throws AccountCreationException;
    AccountDto deleteAccount(int accountId);
    AccountDto addBalance(int accountId, BigDecimal amount);
    AccountDto withdrawMoney(int accountId, BigDecimal amount) throws LowBalanceException;
    List<AccountDto> transferAmount(int fromAccount, int toAccount, BigDecimal amount) throws LowBalanceException, TransferException;
}
