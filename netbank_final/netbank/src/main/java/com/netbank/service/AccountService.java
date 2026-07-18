package com.netbank.service;

import com.netbank.entity.Account;
import com.netbank.entity.User;
import java.util.List;

public interface AccountService {
    List<Account> getAccountsByUser(User user);
    Account getAccountByNumber(String accountNumber);
    Account getAccountByNumberAndUser(String accountNumber, User user);
}
