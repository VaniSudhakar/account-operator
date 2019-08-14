package com.revolut.account;

import com.google.inject.Inject;
import com.revolut.account.route.Router;
import io.javalin.Javalin;

import javax.inject.Singleton;

@Singleton
public class AppBootstrap {

    private Javalin javalinApp;

    @Inject(optional = true)
    private Router appRouter;

    @Inject
    public AppBootstrap(Javalin javalinApp) {
        this.javalinApp = javalinApp;
    }
    private void bindRoutes() {
        appRouter.bindRoutes();
    }

    public void boot(String[] args) {
        bindRoutes();
        javalinApp.start(Integer.parseInt(args[0]));
    }


}
