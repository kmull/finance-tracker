package kmull.finance_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        @NotNull(message = "Kwota jest wymagana")
        @Positive(message = "Kwota musi być większa od zera")
        BigDecimal amount,

        @NotBlank(message = "Kategoria jest wymagana")
        String category,

        String description,

        @NotNull(message = "Data jest wymagana")
        LocalDate date
        ) {
};
