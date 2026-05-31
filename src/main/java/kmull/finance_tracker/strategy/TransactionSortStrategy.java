package kmull.finance_tracker.strategy;

import kmull.finance_tracker.model.Transaction;

import java.util.List;

public interface TransactionSortStrategy {
    List<Transaction> sort(List<Transaction> transactions);
}
