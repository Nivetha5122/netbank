package com.netbank.controller;

import com.netbank.entity.Account;
import com.netbank.entity.Transaction;
import com.netbank.entity.User;
import com.netbank.service.AccountService;
import com.netbank.service.TransactionService;
import com.netbank.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public DashboardController(UserService userService,
                               AccountService accountService,
                               TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<Account> accounts = accountService.getAccountsByUser(user);

        // Collect recent transactions across all accounts
        List<Transaction> recentTransactions = new ArrayList<>();
        for (Account account : accounts) {
            recentTransactions.addAll(transactionService.getMiniStatement(account));
        }
        recentTransactions.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        if (recentTransactions.size() > 5) recentTransactions = recentTransactions.subList(0, 5);

        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);
        model.addAttribute("recentTransactions", recentTransactions);
        return "dashboard/home";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }
}
