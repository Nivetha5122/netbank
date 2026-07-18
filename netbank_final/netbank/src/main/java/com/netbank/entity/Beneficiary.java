package com.netbank.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "beneficiaries",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "account_number"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 16)
    private String accountNumber;

    @Column(nullable = false)
    private String bankName;

    @Column(length = 11)
    private String ifscCode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}
