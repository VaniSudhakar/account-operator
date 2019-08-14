package com.revolut.account.modules;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.revolut.account.controller.AccountController;
import com.revolut.account.modules.AccountDaoModule;
import com.revolut.account.modules.AccountServiceModule;
import com.revolut.account.route.AccountRouter;
import com.revolut.account.route.Router;

public class AccountModule extends AbstractModule {

    protected void configure() {
        bind(AccountController.class);
        install(new AccountServiceModule());
        install(new AccountDaoModule());
        Multibinder.newSetBinder(binder(), Router.class).addBinding().to(AccountRouter.class);
    }
}
