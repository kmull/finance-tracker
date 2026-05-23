package kmull.finance_tracker.dto;

public record AuthRequest(
        String email,
        String password,
        String name
) {
}
