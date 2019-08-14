package com.revolut.account.modules;

import com.google.inject.AbstractModule;
import com.revolut.account.dto.AccountDto;
import com.revolut.account.repository.AccountDao;
import com.revolut.account.repository.AccountDaoImpl;

public class AccountDaoModule extends AbstractModule {

    protected void configure() {
        bind(AccountDao.class).to(AccountDaoImpl.class);
    }
}
