package com.netbank.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BillPaymentRequest {

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Bill type is required")
    private String billType;

    @NotBlank(message = "Biller name is required")
    private String billerName;

    @NotBlank(message = "Consumer number is required")
    private String consumerNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Minimum amount is ₹1")
    private BigDecimal amount;
}
