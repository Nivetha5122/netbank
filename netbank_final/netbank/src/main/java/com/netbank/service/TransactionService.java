package com.netbank.service;

import com.netbank.dto.TransferRequest;
import com.netbank.entity.Account;
import com.netbank.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TransactionService {
    Transaction transfer(TransferRequest request);
    List<Transaction> getMiniStatement(Account account);
    Page<Transaction> getFullStatement(Account account, Pageable pageable);
}
