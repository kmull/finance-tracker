package kmull.finance_tracker.strategy;

import kmull.finance_tracker.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("amountAsc")
public class SortByAmountAsc implements TransactionSortStrategy {

    @Override
    public List<Transaction> sort(List<Transaction> transactions) {
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getAmount))
                .toList();
    }
}
