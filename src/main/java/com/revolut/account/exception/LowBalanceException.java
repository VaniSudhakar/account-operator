package com.revolut.account.exception;

public class LowBalanceException extends Exception {
    /**
     * Exception on Withdrawal or Transfer Attempts with higher amounts than is present in an account.
     * @param message
     */
    public LowBalanceException(String message) {
        super("LowBalanceException-> Account does not have sufficient balance. " + message);
    }
}
