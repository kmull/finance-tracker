package kmull.finance_tracker.exception;

public class BudgetNotFoundException extends RuntimeException{
    public BudgetNotFoundException(Long id) {
        super("Budżet o ID " + id + " nie istnieje");
    }
}
