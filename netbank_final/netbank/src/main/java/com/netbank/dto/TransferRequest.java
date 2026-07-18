package com.netbank.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransferRequest {

    @NotBlank(message = "Source account is required")
    private String sourceAccountNumber;

    @NotBlank(message = "Destination account is required")
    private String destinationAccountNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Minimum transfer amount is ₹1")
    @DecimalMax(value = "1000000.00", message = "Maximum transfer per transaction is ₹10,00,000")
    private BigDecimal amount;

    @Size(max = 200)
    private String description;
}
