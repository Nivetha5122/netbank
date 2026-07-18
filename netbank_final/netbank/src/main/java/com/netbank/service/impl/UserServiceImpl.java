package com.netbank.service.impl;

import com.netbank.dto.RegisterRequest;
import com.netbank.entity.Account;
import com.netbank.entity.User;
import com.netbank.exception.NetBankException;
import com.netbank.repository.AccountRepository;
import com.netbank.repository.UserRepository;
import com.netbank.service.UserService;
import com.netbank.util.AccountUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           AccountRepository accountRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new NetBankException("Passwords do not match");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new NetBankException("Email already registered");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new NetBankException("Phone number already registered");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(User.Role.CUSTOMER)
                .enabled(true)
                .build();
        user = userRepository.save(user);

        // Auto-create account
        String accountNumber;
        do {
            accountNumber = AccountUtils.generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(Account.AccountType.valueOf(request.getAccountType().toUpperCase()))
                .balance(java.math.BigDecimal.valueOf(10000)) // welcome bonus
                .status(Account.AccountStatus.ACTIVE)
                .user(user)
                .build();
        accountRepository.save(account);

        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NetBankException("User not found"));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NetBankException("User not found"));
    }
}
