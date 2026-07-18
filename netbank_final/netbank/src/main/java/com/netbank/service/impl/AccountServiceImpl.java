package com.netbank.service.impl;

import com.netbank.entity.Account;
import com.netbank.entity.User;
import com.netbank.exception.AccountNotFoundException;
import com.netbank.exception.NetBankException;
import com.netbank.repository.AccountRepository;
import com.netbank.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsByUser(User user) {
        return accountRepository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountByNumberAndUser(String accountNumber, User user) {
        Account account = getAccountByNumber(accountNumber);
        if (!account.getUser().getId().equals(user.getId())) {
            throw new NetBankException("Access denied: account does not belong to you");
        }
        return account;
    }
}
