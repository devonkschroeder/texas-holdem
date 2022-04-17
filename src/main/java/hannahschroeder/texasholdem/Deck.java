package hannahschroeder.texasholdem;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

class Deck {
    Stack<Card> cards;

    /** Empty deck */
    public Deck() {
        cards = new Stack<>();
    }

    /**
     * 
     * @param shuffle shuffles the deck if true
     */
    public Deck(boolean shuffle) {
        cards = new Stack<>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }

        if (shuffle) {
            shuffle();
        }
    }

    public static Deck riggedDeck(String cardsOnTop) {
        List<Card> riggedCards = Hand.parse(cardsOnTop).getCards();

        Deck deck = new Deck();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card(rank, suit);
                if (!riggedCards.contains(card)) {
                    deck.cards.add(card);
                }
            }
        }

        Collections.reverse(riggedCards);
        for (Card card : riggedCards) {
            deck.cards.add(card);
        }

        return deck;
    }

    // not currently in use
    public int cardsRemaining() {
        return cards.size();
    }

    public void shuffle() {
        for (int i = 0; i < 4; i++) {
            Collections.shuffle(cards);            
        }
    }

    public Card drawCard() {
        return cards.pop();
    }

    public void burn() {
        cards.pop();
    }

    @Override
    public String toString() {
        String deckString = "";
        for (Card card : cards) {
            deckString += card.toString() +  " ";
        }
        return deckString;
    }
}