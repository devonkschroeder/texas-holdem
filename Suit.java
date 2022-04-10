public enum Suit {
    SPADES("♠"),
    HEARTS("♥"),
    DIAMONDS("♦"),
    CLUBS("♣");

    private String symbol;

    Suit(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
        return symbol;
    }
}