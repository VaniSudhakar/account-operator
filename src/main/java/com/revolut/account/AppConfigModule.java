package com.revolut.account;

import com.google.inject.AbstractModule;
import com.revolut.account.route.AccountRouter;
import com.revolut.account.route.Router;

public class AppConfigModule extends AbstractModule {
    protected void configure() {
        bind(AppBootstrap.class);
        bind(Router.class).to(AccountRouter.class);
    }
}
