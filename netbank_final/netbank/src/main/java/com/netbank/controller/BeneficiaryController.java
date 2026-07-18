package com.netbank.controller;

import com.netbank.entity.User;
import com.netbank.service.BeneficiaryService;
import com.netbank.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/beneficiaries")
public class BeneficiaryController {

    private final UserService userService;
    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(UserService userService, BeneficiaryService beneficiaryService) {
        this.userService = userService;
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping
    public String listPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("beneficiaries", beneficiaryService.getBeneficiaries(user));
        return "dashboard/beneficiaries";
    }

    @PostMapping("/add")
    public String addBeneficiary(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String name,
                                 @RequestParam String accountNumber,
                                 @RequestParam String bankName,
                                 @RequestParam(required = false) String ifscCode,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        try {
            beneficiaryService.addBeneficiary(user, name, accountNumber, bankName, ifscCode);
            redirectAttributes.addFlashAttribute("success", "Beneficiary added successfully");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/beneficiaries";
    }

    @PostMapping("/delete/{id}")
    public String deleteBeneficiary(@PathVariable Long id,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        try {
            beneficiaryService.deleteBeneficiary(id, user);
            redirectAttributes.addFlashAttribute("success", "Beneficiary removed");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/beneficiaries";
    }
}
