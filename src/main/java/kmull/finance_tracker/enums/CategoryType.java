package kmull.finance_tracker.enums;

public enum CategoryType {
    FOOD("Jedzenie"),
    TRANSPORT("Transport"),
    ENTERTAINMENT("Rozrywka"),
    HEALTH("Zdrowie"),
    SHOPPING("Zakupy"),
    OTHER("Inne");

    private String description;

    CategoryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
