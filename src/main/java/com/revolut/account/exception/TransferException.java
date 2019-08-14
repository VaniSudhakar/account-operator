package com.revolut.account.exception;

public class TransferException extends Exception {
    /**
     * Custom Exception Messages to be passed.
     * @param message
     */
    public TransferException(String message){
        super("TransferException-> "+ message);
    }
}
