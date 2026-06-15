package kmull.finance_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetRequest(
        @NotBlank String category,
        @NotNull @Positive BigDecimal limit,
        @NotNull LocalDate month
) {
}
