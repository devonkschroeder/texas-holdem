package hannahschroeder.texasholdem;

import java.util.Collections;
import java.util.Stack;

class Deck {
    Stack<Card> deck;

    public Deck(boolean shuffle) {
        deck = new Stack<>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }

        if (shuffle) {
            shuffle();
        }
    }

    // not currently in use
    public int cardsRemaining() {
        return deck.size();
    }

    public void shuffle() {
        for (int i = 0; i < 4; i++) {
            Collections.shuffle(deck);            
        }
    }

    public Card drawCard() {
        return deck.pop();
    }

    public void burn() {
        deck.pop();
    }

    @Override
    public String toString() {
        String deckString = "";
        for (Card card : deck) {
            deckString += card.toString() +  " ";
        }
        return deckString;
    }
}