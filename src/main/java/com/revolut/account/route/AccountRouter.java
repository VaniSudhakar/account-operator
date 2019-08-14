package com.revolut.account.route;

import com.revolut.account.controller.AccountController;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.post;

@Singleton
public class AccountRouter extends Router<AccountController> {

    private Javalin accountApp;

    @Inject
    public AccountRouter(Javalin accountApp) {
        this.accountApp = accountApp;
    }

    @Override
    public void bindRoutes() {

        accountApp.routes(() -> {
            get("/api/operation/allAccounts", ctxt -> getController().getAllAccounts(ctxt));
            post("/api/operation/create", ctxt -> getController().createAccount(ctxt));
            delete("/api/operation/delete/:account-id", ctxt -> getController().deleteAccount(ctxt, ctxt.pathParam("account-id")));
            get("/api/operation/balance/:account-id", ctxt -> getController().getAccountBalance(ctxt, ctxt.pathParam("account-id")));
            put("/api/operation/addBalance", ctxt -> getController().addMoney(ctxt));
            put("/api/operation/withdraw", ctxt -> getController().withdrawMoney(ctxt));
            put("/api/operation/transfer", ctxt -> getController().transferAmount(ctxt));

        });
    }
}
