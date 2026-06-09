package kmull.finance_tracker.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        String category,
        String description,
        LocalDate date
) implements Serializable {
}
