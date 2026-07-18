package com.netbank.service.impl;

import com.netbank.dto.TransferRequest;
import com.netbank.entity.Account;
import com.netbank.entity.Transaction;
import com.netbank.exception.AccountNotFoundException;
import com.netbank.exception.InsufficientFundsException;
import com.netbank.exception.NetBankException;
import com.netbank.repository.AccountRepository;
import com.netbank.repository.TransactionRepository;
import com.netbank.service.TransactionService;
import com.netbank.util.AccountUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountRepository accountRepository,
                                  TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public Transaction transfer(TransferRequest request) {
        if (request.getSourceAccountNumber().equals(request.getDestinationAccountNumber())) {
            throw new NetBankException("Source and destination accounts cannot be the same");
        }

        // Deadlock-safe: always lock in ascending account number order
        String first = request.getSourceAccountNumber().compareTo(request.getDestinationAccountNumber()) < 0
                ? request.getSourceAccountNumber() : request.getDestinationAccountNumber();
        String second = first.equals(request.getSourceAccountNumber())
                ? request.getDestinationAccountNumber() : request.getSourceAccountNumber();

        Account accountA = accountRepository.findByAccountNumberWithLock(first)
                .orElseThrow(() -> new AccountNotFoundException(first));
        Account accountB = accountRepository.findByAccountNumberWithLock(second)
                .orElseThrow(() -> new AccountNotFoundException(second));

        Account source = accountA.getAccountNumber().equals(request.getSourceAccountNumber()) ? accountA : accountB;
        Account dest   = accountA.getAccountNumber().equals(request.getSourceAccountNumber()) ? accountB : accountA;

        if (source.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new NetBankException("Source account is not active");
        }
        if (dest.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new NetBankException("Destination account is not active");
        }
        if (source.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException(source.getBalance(), request.getAmount());
        }

        // Debit source
        source.setBalance(source.getBalance().subtract(request.getAmount()));
        accountRepository.save(source);

        // Credit destination
        dest.setBalance(dest.getBalance().add(request.getAmount()));
        accountRepository.save(dest);

        // Generate unique reference
        String ref;
        do {
            ref = AccountUtils.generateReferenceNumber();
        } while (transactionRepository.existsByReferenceNumber(ref));

        Transaction txn = Transaction.builder()
                .referenceNumber(ref)
                .type(Transaction.TransactionType.TRANSFER)
                .amount(request.getAmount())
                .balanceAfter(source.getBalance())
                .description(request.getDescription() != null ? request.getDescription() : "Fund Transfer")
                .status(Transaction.TransactionStatus.SUCCESS)
                .sourceAccount(source)
                .destinationAccount(dest)
                .build();

        return transactionRepository.save(txn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getMiniStatement(Account account) {
        Pageable top5 = PageRequest.of(0, 5);
        return transactionRepository.findTop5ByAccount(account, top5);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> getFullStatement(Account account, Pageable pageable) {
        return transactionRepository.findAllByAccountPageable(account, pageable);
    }
}
