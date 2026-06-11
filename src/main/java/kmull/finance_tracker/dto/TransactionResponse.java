package kmull.finance_tracker.dto;

import kmull.finance_tracker.enums.CategoryType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        CategoryType category,
        String categoryDescription,
        String description,
        LocalDate date
) implements Serializable {
}
