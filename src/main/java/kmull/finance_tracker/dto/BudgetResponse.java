package kmull.finance_tracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetResponse(
        Long id,
        String category,
        String categoryDescription,
        BigDecimal limit,
        LocalDate month,
        BigDecimal totalSpent,
        boolean exceeded
) {
}
