package com.netbank.controller;

import com.netbank.dto.ApiResponse;
import com.netbank.dto.BillPaymentRequest;
import com.netbank.dto.TransferRequest;
import com.netbank.entity.*;
import com.netbank.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST API layer — 12+ endpoints for all banking operations.
 * Consumes/produces JSON for potential frontend or mobile integration.
 */
@RestController
@RequestMapping("/api/v1")
public class AccountApiController {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final BeneficiaryService beneficiaryService;
    private final BillPaymentService billPaymentService;

    public AccountApiController(UserService userService, AccountService accountService,
                                TransactionService transactionService, BeneficiaryService beneficiaryService,
                                BillPaymentService billPaymentService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.beneficiaryService = beneficiaryService;
        this.billPaymentService = billPaymentService;
    }

    // ─── ACCOUNT ENDPOINTS ─────────────────────────────────────────────────────

    /** GET /api/v1/accounts — list all accounts for logged-in user */
    @GetMapping("/accounts")
    public ResponseEntity<ApiResponse<List<Account>>> getAccounts(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<Account> accounts = accountService.getAccountsByUser(user);
        return ResponseEntity.ok(ApiResponse.success("Accounts fetched", accounts));
    }

    /** GET /api/v1/accounts/{accountNumber} — get specific account details */
    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<ApiResponse<Account>> getAccount(
            @PathVariable String accountNumber,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Account account = accountService.getAccountByNumberAndUser(accountNumber, user);
        return ResponseEntity.ok(ApiResponse.success("Account fetched", account));
    }

    /** GET /api/v1/accounts/{accountNumber}/balance — get balance */
    @GetMapping("/accounts/{accountNumber}/balance")
    public ResponseEntity<ApiResponse<java.math.BigDecimal>> getBalance(
            @PathVariable String accountNumber,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Account account = accountService.getAccountByNumberAndUser(accountNumber, user);
        return ResponseEntity.ok(ApiResponse.success("Balance fetched", account.getBalance()));
    }

    // ─── TRANSACTION ENDPOINTS ─────────────────────────────────────────────────

    /** POST /api/v1/transactions/transfer — initiate fund transfer */
    @PostMapping("/transactions/transfer")
    public ResponseEntity<ApiResponse<Transaction>> transfer(
            @Valid @RequestBody TransferRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        accountService.getAccountByNumberAndUser(request.getSourceAccountNumber(), user);
        Transaction txn = transactionService.transfer(request);
        return ResponseEntity.ok(ApiResponse.success("Transfer successful", txn));
    }

    /** GET /api/v1/transactions/mini?accountNumber=XX — mini statement (5 txns) */
    @GetMapping("/transactions/mini")
    public ResponseEntity<ApiResponse<List<Transaction>>> getMiniStatement(
            @RequestParam String accountNumber,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Account account = accountService.getAccountByNumberAndUser(accountNumber, user);
        List<Transaction> txns = transactionService.getMiniStatement(account);
        return ResponseEntity.ok(ApiResponse.success("Mini statement fetched", txns));
    }

    /** GET /api/v1/transactions/full?accountNumber=XX&page=0 — paginated full statement */
    @GetMapping("/transactions/full")
    public ResponseEntity<ApiResponse<Page<Transaction>>> getFullStatement(
            @RequestParam String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Account account = accountService.getAccountByNumberAndUser(accountNumber, user);
        var pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Transaction> result = transactionService.getFullStatement(account, pageable);
        return ResponseEntity.ok(ApiResponse.success("Statement fetched", result));
    }

    // ─── BENEFICIARY ENDPOINTS ─────────────────────────────────────────────────

    /** GET /api/v1/beneficiaries — list beneficiaries */
    @GetMapping("/beneficiaries")
    public ResponseEntity<ApiResponse<List<Beneficiary>>> getBeneficiaries(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Beneficiaries fetched",
                beneficiaryService.getBeneficiaries(user)));
    }

    /** POST /api/v1/beneficiaries — add beneficiary */
    @PostMapping("/beneficiaries")
    public ResponseEntity<ApiResponse<Beneficiary>> addBeneficiary(
            @RequestParam String name,
            @RequestParam String accountNumber,
            @RequestParam String bankName,
            @RequestParam(required = false) String ifscCode,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Beneficiary b = beneficiaryService.addBeneficiary(user, name, accountNumber, bankName, ifscCode);
        return ResponseEntity.ok(ApiResponse.success("Beneficiary added", b));
    }

    /** DELETE /api/v1/beneficiaries/{id} — remove beneficiary */
    @DeleteMapping("/beneficiaries/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBeneficiary(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        beneficiaryService.deleteBeneficiary(id, user);
        return ResponseEntity.ok(ApiResponse.success("Beneficiary removed", null));
    }

    // ─── BILL PAYMENT ENDPOINTS ────────────────────────────────────────────────

    /** POST /api/v1/bills/pay — pay a bill */
    @PostMapping("/bills/pay")
    public ResponseEntity<ApiResponse<BillPayment>> payBill(
            @Valid @RequestBody BillPaymentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        accountService.getAccountByNumberAndUser(request.getAccountNumber(), user);
        BillPayment bill = billPaymentService.payBill(request);
        return ResponseEntity.ok(ApiResponse.success("Bill paid successfully", bill));
    }

    /** GET /api/v1/bills?accountNumber=XX — bill payment history */
    @GetMapping("/bills")
    public ResponseEntity<ApiResponse<List<BillPayment>>> getBillHistory(
            @RequestParam String accountNumber,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Account account = accountService.getAccountByNumberAndUser(accountNumber, user);
        return ResponseEntity.ok(ApiResponse.success("Bill history fetched",
                billPaymentService.getBillHistory(account)));
    }

    // ─── USER ENDPOINT ─────────────────────────────────────────────────────────

    /** GET /api/v1/user/profile — get current user profile */
    @GetMapping("/user/profile")
    public ResponseEntity<ApiResponse<User>> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Profile fetched", user));
    }
}
