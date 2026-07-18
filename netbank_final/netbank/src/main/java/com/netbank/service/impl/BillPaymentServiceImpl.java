package com.netbank.service.impl;

import com.netbank.dto.BillPaymentRequest;
import com.netbank.entity.Account;
import com.netbank.entity.BillPayment;
import com.netbank.entity.Transaction;
import com.netbank.exception.InsufficientFundsException;
import com.netbank.exception.NetBankException;
import com.netbank.repository.AccountRepository;
import com.netbank.repository.BillPaymentRepository;
import com.netbank.repository.TransactionRepository;
import com.netbank.service.AccountService;
import com.netbank.service.BillPaymentService;
import com.netbank.util.AccountUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BillPaymentServiceImpl implements BillPaymentService {

    private final AccountRepository accountRepository;
    private final BillPaymentRepository billPaymentRepository;
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public BillPaymentServiceImpl(AccountRepository accountRepository,
                                  BillPaymentRepository billPaymentRepository,
                                  TransactionRepository transactionRepository,
                                  AccountService accountService) {
        this.accountRepository = accountRepository;
        this.billPaymentRepository = billPaymentRepository;
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Override
    @Transactional
    public BillPayment payBill(BillPaymentRequest request) {
        Account account = accountService.getAccountByNumber(request.getAccountNumber());

        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new NetBankException("Account is not active");
        }
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException(account.getBalance(), request.getAmount());
        }

        // Debit account
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        String ref = AccountUtils.generateBillReference();

        // Record bill payment
        BillPayment bill = BillPayment.builder()
                .billType(BillPayment.BillType.valueOf(request.getBillType().toUpperCase()))
                .billerName(request.getBillerName())
                .consumerNumber(request.getConsumerNumber())
                .amount(request.getAmount())
                .referenceNumber(ref)
                .status(BillPayment.PaymentStatus.SUCCESS)
                .account(account)
                .build();
        billPaymentRepository.save(bill);

        // Record transaction
        String txnRef;
        do { txnRef = AccountUtils.generateReferenceNumber(); }
        while (transactionRepository.existsByReferenceNumber(txnRef));

        Transaction txn = Transaction.builder()
                .referenceNumber(txnRef)
                .type(Transaction.TransactionType.BILL_PAYMENT)
                .amount(request.getAmount())
                .balanceAfter(account.getBalance())
                .description("Bill Payment: " + request.getBillType() + " - " + request.getBillerName())
                .status(Transaction.TransactionStatus.SUCCESS)
                .sourceAccount(account)
                .build();
        transactionRepository.save(txn);

        return bill;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillPayment> getBillHistory(Account account) {
        return billPaymentRepository.findByAccountOrderByPaidAtDesc(account);
    }
}
