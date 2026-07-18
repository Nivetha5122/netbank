package com.netbank.controller;

import com.netbank.dto.BillPaymentRequest;
import com.netbank.entity.BillPayment;
import com.netbank.entity.User;
import com.netbank.service.AccountService;
import com.netbank.service.BillPaymentService;
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
@RequestMapping("/bills")
public class BillPaymentController {

    private final UserService userService;
    private final AccountService accountService;
    private final BillPaymentService billPaymentService;

    public BillPaymentController(UserService userService, AccountService accountService,
                                 BillPaymentService billPaymentService) {
        this.userService = userService;
        this.accountService = accountService;
        this.billPaymentService = billPaymentService;
    }

    @GetMapping
    public String billPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("accounts", accountService.getAccountsByUser(user));
        model.addAttribute("billPaymentRequest", new BillPaymentRequest());
        model.addAttribute("billTypes", com.netbank.entity.BillPayment.BillType.values());
        return "dashboard/bills";
    }

    @PostMapping("/pay")
    public String payBill(@Valid @ModelAttribute BillPaymentRequest request,
                          BindingResult result,
                          @AuthenticationPrincipal UserDetails userDetails,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("accounts", accountService.getAccountsByUser(user));
            model.addAttribute("billTypes", com.netbank.entity.BillPayment.BillType.values());
            return "dashboard/bills";
        }
        try {
            BillPayment bill = billPaymentService.payBill(request);
            redirectAttributes.addFlashAttribute("success",
                    "Bill paid successfully! Reference: " + bill.getReferenceNumber());
            return "redirect:/bills";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("accounts", accountService.getAccountsByUser(user));
            model.addAttribute("billTypes", com.netbank.entity.BillPayment.BillType.values());
            return "dashboard/bills";
        }
    }
}
