package kmull.finance_tracker.exception;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(Long id) {
        super("Nie znaleziono transakcji o id: " + id);
    }
}
