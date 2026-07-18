package com.netbank.controller;

import com.netbank.entity.Account;
import com.netbank.entity.User;
import com.netbank.service.AccountService;
import com.netbank.service.TransactionService;
import com.netbank.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/statement")
public class StatementController {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public StatementController(UserService userService, AccountService accountService,
                               TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/mini")
    public String miniStatement(@RequestParam String accountNumber,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        Account account = accountService.getAccountByNumberAndUser(accountNumber, user);
        model.addAttribute("user", user);
        model.addAttribute("account", account);
        model.addAttribute("accounts", accountService.getAccountsByUser(user));
        model.addAttribute("transactions", transactionService.getMiniStatement(account));
        model.addAttribute("isMini", true);
        return "statement/statement";
    }

    @GetMapping("/full")
    public String fullStatement(@RequestParam String accountNumber,
                                @RequestParam(defaultValue = "0") int page,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        Account account = accountService.getAccountByNumberAndUser(accountNumber, user);
        var pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        model.addAttribute("user", user);
        model.addAttribute("account", account);
        model.addAttribute("accounts", accountService.getAccountsByUser(user));
        model.addAttribute("transactionPage", transactionService.getFullStatement(account, pageable));
        model.addAttribute("isMini", false);
        model.addAttribute("currentPage", page);
        return "statement/statement";
    }
}
