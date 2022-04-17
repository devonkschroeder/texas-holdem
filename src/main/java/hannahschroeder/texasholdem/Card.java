package hannahschroeder.texasholdem;

class Card implements Comparable<Card>{
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank rank() {
        return rank;
    }

    public Suit suit() {
        return suit;
    }

    public int value() {
        return rank.value();
    }

    public String toString() {
        return rank.toString() + suit.toString();
    }

    @Override
    public int compareTo(Card card) {
        return this.value() - card.value();
    }

    public static Card parse(String text) {
        Rank rank = Rank.parse(text.substring(0, 1));
        Suit suit = Suit.parse(text.substring(1, 2));

        return new Card(rank, suit);
    }
}