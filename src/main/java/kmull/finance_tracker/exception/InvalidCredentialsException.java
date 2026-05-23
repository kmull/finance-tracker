package kmull.finance_tracker.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Nieprawidłowy email lub hasło");
    }
}
