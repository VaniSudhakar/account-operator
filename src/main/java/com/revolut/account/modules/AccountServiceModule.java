package com.revolut.account.modules;

import com.google.inject.AbstractModule;
import com.revolut.account.services.AccountService;
import com.revolut.account.services.impl.AccountServiceImpl;

public class AccountServiceModule extends AbstractModule {

    protected void configure() {
        bind(AccountService.class).to(AccountServiceImpl.class);
    }
}
