package com.crypto.watcher.crypto_watcher.dto.request;

import com.crypto.watcher.crypto_watcher.enums.AlertCondition;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateAlertRequest(
        @NotBlank(message = "Symbol cannot be empty")
        String symbol,
        @NotNull(message = "Target price is required")
        @Positive(message = "Target Price must be greater than zero")
        BigDecimal targetPrice,
        @NotNull(message = "Condition is required")
        AlertCondition condition,

        @Email(message = "Please provide a valid email address")
        @NotNull(message = "User email is required")
        String userEmail
) {
}
