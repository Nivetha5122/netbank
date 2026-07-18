package com.netbank.service;

import com.netbank.dto.BillPaymentRequest;
import com.netbank.entity.Account;
import com.netbank.entity.BillPayment;
import java.util.List;

public interface BillPaymentService {
    BillPayment payBill(BillPaymentRequest request);
    List<BillPayment> getBillHistory(Account account);
}
