package com.netbank.exception;

public class AccountNotFoundException extends NetBankException {
    public AccountNotFoundException(String accountNumber) {
        super("Account not found: " + accountNumber);
    }
}
