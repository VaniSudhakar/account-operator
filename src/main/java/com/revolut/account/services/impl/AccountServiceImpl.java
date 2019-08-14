package com.revolut.account.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.account.dto.AccountDto;
import com.revolut.account.dto.AccountInputRequest;
import com.revolut.account.entity.Account;
import com.revolut.account.exception.AccountCreationException;
import com.revolut.account.exception.LowBalanceException;
import com.revolut.account.exception.TransferException;
import com.revolut.account.repository.AccountDao;
import com.revolut.account.services.AccountService;

import java.math.BigDecimal;
import java.util.List;

@Singleton
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    @Inject
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @Override
    public AccountDto createAccount(AccountInputRequest accountInputRequest) throws AccountCreationException {
        return accountDao.createAccount(accountInputRequest);
    }

    @Override
    public AccountDto deleteAccount(int accountId) {
        return accountDao.deleteAccount(accountId);
    }

    @Override
    public AccountDto addBalance(int accountId, BigDecimal amount) {
        return accountDao.addBalance(accountId,amount);
    }

    @Override
    public AccountDto withdrawMoney(int accountId, BigDecimal amount) throws LowBalanceException {
        return accountDao.withdrawAmount(accountId,amount);
    }

    @Override
    public List<AccountDto> transferAmount(int fromAccount, int toAccount, BigDecimal amount) throws LowBalanceException, TransferException {
        return accountDao.transferAmount(fromAccount,toAccount, amount);
    }
}
