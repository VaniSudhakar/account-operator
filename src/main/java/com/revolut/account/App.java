package com.revolut.account;

import com.google.inject.Guice;

/**
 * <code>Main class to startup the application {@link App}</code>
 */
public class App {
    public static void main(String[] args) {
        Guice.createInjector(new AppConfigModule()).getInstance(AppBootstrap.class).boot(args);
    }
}
