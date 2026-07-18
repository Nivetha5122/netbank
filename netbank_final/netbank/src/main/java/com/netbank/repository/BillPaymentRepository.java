package com.netbank.repository;

import com.netbank.entity.Account;
import com.netbank.entity.BillPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {
    List<BillPayment> findByAccountOrderByPaidAtDesc(Account account);
}
