package mkull.finance_tracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        String category,
        String description,
        LocalDate date
) {
}
