package hannahschroeder.texasholdem;

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

    public static Suit parse(String text) {
        Suit suit;
        switch (text) {
            case "♠":
                suit = SPADES;
                break;
            case "♥":
                suit = HEARTS;
                break;
            case "♦":
                suit = DIAMONDS;
                break;
            case "♣":
                suit = CLUBS;
                break;
            default:
                throw new RuntimeException("text is not a valid suit");
        }
        return suit;
    }
}