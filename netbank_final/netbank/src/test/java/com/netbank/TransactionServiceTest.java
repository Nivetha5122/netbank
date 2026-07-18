package com.netbank;

import com.netbank.dto.TransferRequest;
import com.netbank.entity.Account;
import com.netbank.entity.Transaction;
import com.netbank.entity.User;
import com.netbank.exception.InsufficientFundsException;
import com.netbank.repository.AccountRepository;
import com.netbank.repository.TransactionRepository;
import com.netbank.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock private AccountRepository accountRepository;
    @Mock private TransactionRepository transactionRepository;
    @InjectMocks private TransactionServiceImpl transactionService;

    private Account sourceAccount;
    private Account destAccount;

    @BeforeEach
    void setUp() {
        User user = User.builder().id(1L).fullName("Test User").email("test@netbank.com").build();

        sourceAccount = Account.builder()
                .id(1L).accountNumber("NB00000000000001")
                .balance(BigDecimal.valueOf(50000))
                .status(Account.AccountStatus.ACTIVE)
                .accountType(Account.AccountType.SAVINGS)
                .user(user).build();

        destAccount = Account.builder()
                .id(2L).accountNumber("NB00000000000002")
                .balance(BigDecimal.valueOf(10000))
                .status(Account.AccountStatus.ACTIVE)
                .accountType(Account.AccountType.SAVINGS)
                .user(user).build();
    }

    @Test
    @DisplayName("Successful transfer debits source and credits destination")
    void transfer_success() {
        when(accountRepository.findByAccountNumberWithLock("NB00000000000001"))
                .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumberWithLock("NB00000000000002"))
                .thenReturn(Optional.of(destAccount));
        when(transactionRepository.existsByReferenceNumber(any())).thenReturn(false);
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TransferRequest req = TransferRequest.builder()
                .sourceAccountNumber("NB00000000000001")
                .destinationAccountNumber("NB00000000000002")
                .amount(BigDecimal.valueOf(5000))
                .description("Test transfer")
                .build();

        Transaction txn = transactionService.transfer(req);

        assertEquals(BigDecimal.valueOf(45000), sourceAccount.getBalance());
        assertEquals(BigDecimal.valueOf(15000), destAccount.getBalance());
        assertEquals(Transaction.TransactionStatus.SUCCESS, txn.getStatus());
        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    @DisplayName("Transfer fails when source has insufficient funds")
    void transfer_insufficientFunds() {
        when(accountRepository.findByAccountNumberWithLock("NB00000000000001"))
                .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumberWithLock("NB00000000000002"))
                .thenReturn(Optional.of(destAccount));

        TransferRequest req = TransferRequest.builder()
                .sourceAccountNumber("NB00000000000001")
                .destinationAccountNumber("NB00000000000002")
                .amount(BigDecimal.valueOf(999999))
                .build();

        assertThrows(InsufficientFundsException.class, () -> transactionService.transfer(req));
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Transfer to same account is rejected")
    void transfer_sameAccount_rejected() {
        TransferRequest req = TransferRequest.builder()
                .sourceAccountNumber("NB00000000000001")
                .destinationAccountNumber("NB00000000000001")
                .amount(BigDecimal.valueOf(1000))
                .build();

        assertThrows(com.netbank.exception.NetBankException.class, () -> transactionService.transfer(req));
    }
}
