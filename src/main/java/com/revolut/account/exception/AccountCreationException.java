package com.revolut.account.exception;

public class AccountCreationException extends Exception{

    /**
     * Custom Exception Messages to be passed on Account Creation issues.
     * @param message
     */
    public AccountCreationException(String message){
        super("AccountCreationException-> "+ message);
    }
}
