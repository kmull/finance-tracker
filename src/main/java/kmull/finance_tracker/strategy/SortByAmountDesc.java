package kmull.finance_tracker.strategy;

import kmull.finance_tracker.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("amountDesc")
public class SortByAmountDesc implements TransactionSortStrategy {

    @Override
    public List<Transaction> sort(List<Transaction> transactions) {
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .toList();
    }
}
