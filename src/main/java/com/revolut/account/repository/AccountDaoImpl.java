package com.revolut.account.repository;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.revolut.account.dto.AccountDto;
import com.revolut.account.dto.AccountInputRequest;
import com.revolut.account.entity.Account;
import com.revolut.account.exception.LowBalanceException;
import com.revolut.account.exception.TransferException;
import org.h2.engine.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.AccountListToAccountDtoListConverter;
import util.AccountReqToAccountEntityConverter;
import util.AccountToAccountDtoConverter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

@Singleton
public class AccountDaoImpl implements AccountDao {

    private AccountListToAccountDtoListConverter listConverter;
    private AccountReqToAccountEntityConverter reqConverter;
    private AccountToAccountDtoConverter dtoConverter;

    @Inject
    public AccountDaoImpl(AccountListToAccountDtoListConverter listConverter, AccountReqToAccountEntityConverter reqConverter, AccountToAccountDtoConverter dtoConverter) {
        this.listConverter = listConverter;
        this.reqConverter = reqConverter;
        this.dtoConverter = dtoConverter;
    }

    private ReentrantLock lock = new ReentrantLock(true);

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDaoImpl.class);

    @Inject
    private Provider<EntityManager> acc;

    private Session getSession() {
        return acc.get().unwrap(Session.class);
    }

    @Override
    public List<AccountDto> getAllAccounts() {

        List<AccountDto> accountDtos = null;
        try {
            LOGGER.info(" Collecting all account information ");
            lock.lock();
            CriteriaQuery<Account> createQuery = acc.get().getCriteriaBuilder().createQuery(Account.class);
            Root<Account> root = createQuery.from(Account.class);
            accountDtos = listConverter.apply(acc.get().createQuery(createQuery).getResultList());
        } finally {
            lock.unlock();
        }
        return accountDtos;
    }

    @Override
    public AccountDto createAccount(AccountInputRequest accountRequest) {
        AccountDto accountDto = null;
        try {
            LOGGER.info("Creating new account " + accountRequest);
            lock.lock();
            Account account = reqConverter.apply(accountRequest);
            accountDto = dtoConverter.apply(acc.get().merge(account));
        } finally {
            lock.unlock();
        }
        return accountDto;
    }

    @Override
    public AccountDto deleteAccount(int accountId) {
        AccountDto accountDto = null;
        try {
            LOGGER.info(" Deleting account id ", accountId);
            Account account = find(accountId);
            acc.get().remove(acc.get().merge(account));
            accountDto = dtoConverter.apply(account);
            lock.lock();

        } finally {
            lock.unlock();
        }
        return accountDto;
    }

    private Account find(int accountId) {
        return acc.get().find(Account.class, accountId);
    }

    @Override
    public AccountDto addBalance(int accountId, BigDecimal amount) {
        AccountDto accountDto = null;
        try {
            Account account = find(accountId);
            BigDecimal balance = account.getBalance().get();
            if (account != null) {
                balance = balance.add(amount);
                account.setBalance(new AtomicReference<>(balance));
                accountDto = dtoConverter.apply(account);
            } else {
                LOGGER.info("Account not found");
            }
        } finally {
            lock.unlock();
        }
        return accountDto;
    }

    @Override
    public AccountDto withdrawAmount(int accountId, BigDecimal amount) throws LowBalanceException {
        AccountDto accountDto = null;
        try {
            Account account = find(accountId);
            BigDecimal balance = account.getBalance().get();
            if (account != null) {
                if (account.balance.get().doubleValue() > amount.doubleValue()) {
                    balance = balance.subtract(amount);
                    account.setBalance(new AtomicReference<>(balance));
                    accountDto = dtoConverter.apply(account);
                } else {
                    throw new LowBalanceException("Do Not have Sufficient Balance!");
                }
            } else {
                LOGGER.info("Account not found");
            }
        } finally {
            lock.unlock();
        }
        return accountDto;
    }

    @Override
    public List<AccountDto> transferAmount(int fromAccount, int toAccount, BigDecimal amount) throws LowBalanceException, TransferException {
        LOGGER.info(" Transferring amount " + amount + " from account " + fromAccount + " to account " + toAccount);
        lock.lock();
        if (fromAccount == toAccount) {
            throw new TransferException("From and To Accounts Cannot be the same!");
        }
        Account fromAcc = find(fromAccount);
        Account toAcc = find(toAccount);
        if (null == fromAcc || null == toAcc) {
            throw new TransferException("Check the Account Numbers. At least one isn't existing!");
        }
        try {
            Double balanceToTransfer = (fromAcc.balance.get().subtract(amount)).doubleValue();
            if (0 > balanceToTransfer) {
                throw new LowBalanceException("Do Not have Sufficient Balance!");
            } else {
                BigDecimal fromBal = fromAcc.getBalance().get().subtract(amount);
                BigDecimal toBal = toAcc.getBalance().get().add(amount);
                fromAcc.setBalance(new AtomicReference<>(fromBal));
                toAcc.setBalance(new AtomicReference<>(toBal));
            }
        } finally {
            lock.unlock();
        }
        List<AccountDto> accountDtos = new ArrayList<>();
        accountDtos.add(dtoConverter.apply(fromAcc));
        accountDtos.add(dtoConverter.apply(toAcc));
        return accountDtos;
    }

    private String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        return now.format(formatter);
    }

}
