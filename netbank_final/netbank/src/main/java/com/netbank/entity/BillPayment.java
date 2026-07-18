package com.netbank.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bill_payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BillPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillType billType;

    @Column(nullable = false)
    private String billerName;

    @Column(nullable = false)
    private String consumerNumber;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 20)
    private String referenceNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.SUCCESS;

    @Column(nullable = false, updatable = false)
    private LocalDateTime paidAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @PrePersist
    protected void onCreate() {
        paidAt = LocalDateTime.now();
    }

    public enum BillType {
        ELECTRICITY, WATER, GAS, INTERNET, MOBILE, DTH, INSURANCE, OTHER
    }

    public enum PaymentStatus {
        SUCCESS, FAILED, PENDING
    }
}
