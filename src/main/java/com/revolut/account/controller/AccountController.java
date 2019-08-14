package com.revolut.account.controller;

import com.revolut.account.dto.AccountDto;
import com.revolut.account.dto.AccountInputRequest;
import com.revolut.account.dto.AccountTransferRequest;
import com.revolut.account.exception.AccountCreationException;
import com.revolut.account.exception.LowBalanceException;
import com.revolut.account.exception.TransferException;
import com.revolut.account.services.AccountService;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class AccountController {

    private AccountService accountService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    public void getAllAccounts(Context context) {
        LOGGER.info("Display all accounts info");
        context.json(accountService.getAllAccounts());
    }

    public void createAccount(Context context) throws AccountCreationException {
        LOGGER.info("Creating Account");
        var accountReq = context.bodyAsClass(AccountInputRequest.class);
        if(accountReq.getAmount().doubleValue() >= 0d) {
            var account = accountService.createAccount(accountReq);
            context.json(account);
            LOGGER.info("Account created ", account);
            context.status(HttpStatus.CREATED_201);
        } else {
            context.status(HttpStatus.EXPECTATION_FAILED_417);
        }
    }

    public void deleteAccount(Context context, String id) {
        accountService.deleteAccount(Integer.parseInt(id));
        context.status(HttpStatus.OK_200);
    }

    public void getAccountBalance(Context context, String accountId) {
        List<AccountDto> accounts = accountService.getAllAccounts().stream()
                .filter(a -> a.getId() == Integer.parseInt(accountId)).collect(Collectors.toList());
        context.json(accounts);
    }

    public void transferAmount(Context context) throws TransferException, LowBalanceException {
        var transReq = context.bodyAsClass(AccountTransferRequest.class);
        LOGGER.info("Transfer initiated " + transReq.getAmount() + " from " + transReq.getAccountFrom() + " to " + transReq.getAccountTo());
        context.json(accountService.transferAmount(transReq.getAccountFrom(), transReq.getAccountTo(), transReq.getAmount()));
        context.status(HttpStatus.OK_200);
    }

    public void addMoney(Context context) {
        var account = context.bodyAsClass(AccountInputRequest.class);
        LOGGER.info("Adding amount ", account.getAmount() + " to account id ", account.getId());
        accountService.addBalance(account.getId(), account.getAmount());
        context.json(accountService.getAllAccounts().stream().filter( a -> a.getId()==account.getId()).collect(Collectors.toList()));
    }

    public void withdrawMoney(Context context) throws LowBalanceException {
        var account = context.bodyAsClass(AccountInputRequest.class);
        LOGGER.info("Withdrawing amount ", account.getAmount() + " to account id ", account.getId());
        accountService.withdrawMoney(account.getId(), account.getAmount());
        context.json(accountService.getAllAccounts().stream().filter( aa -> aa.getId()==account.getId()).collect(Collectors.toList()));
    }
}
