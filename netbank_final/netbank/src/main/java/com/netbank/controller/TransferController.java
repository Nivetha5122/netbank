package com.netbank.controller;

import com.netbank.dto.TransferRequest;
import com.netbank.entity.Transaction;
import com.netbank.entity.User;
import com.netbank.service.AccountService;
import com.netbank.service.BeneficiaryService;
import com.netbank.service.TransactionService;
import com.netbank.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final BeneficiaryService beneficiaryService;

    public TransferController(UserService userService, AccountService accountService,
                              TransactionService transactionService, BeneficiaryService beneficiaryService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping
    public String transferPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("accounts", accountService.getAccountsByUser(user));
        model.addAttribute("beneficiaries", beneficiaryService.getBeneficiaries(user));
        model.addAttribute("transferRequest", new TransferRequest());
        return "transfer/transfer";
    }

    @PostMapping
    public String doTransfer(@Valid @ModelAttribute TransferRequest request,
                             BindingResult result,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("accounts", accountService.getAccountsByUser(user));
            model.addAttribute("beneficiaries", beneficiaryService.getBeneficiaries(user));
            return "transfer/transfer";
        }
        try {
            // Validate source account belongs to user
            accountService.getAccountByNumberAndUser(request.getSourceAccountNumber(), user);
            Transaction txn = transactionService.transfer(request);
            redirectAttributes.addFlashAttribute("success", "Transfer successful! Reference: " + txn.getReferenceNumber());
            return "redirect:/transfer/success/" + txn.getReferenceNumber();
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("accounts", accountService.getAccountsByUser(user));
            model.addAttribute("beneficiaries", beneficiaryService.getBeneficiaries(user));
            return "transfer/transfer";
        }
    }

    @GetMapping("/success/{ref}")
    public String transferSuccess(@PathVariable String ref,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("referenceNumber", ref);
        return "transfer/success";
    }
}
