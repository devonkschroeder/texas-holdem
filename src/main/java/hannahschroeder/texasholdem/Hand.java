package hannahschroeder.texasholdem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Hand implements Comparable<Hand> {
    static int FIVE_CARD_HAND = 5;

    private List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public Hand(List<Card> start) {
        cards = start;
    }

    public int size() {
        return cards.size();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void clearHand() {
        cards.clear();
    }

    public List<Card> getCards() {
        return cards;
    }

    public Card highCard() {
        List<Card> cards = copyCards();
        return Collections.max(cards);
    }

    public int[] getSortedRankValues() {
        List<Card> sortedCards = copyCards();
        Collections.sort(sortedCards);
        Collections.reverse(sortedCards);
        int[] values = new int[sortedCards.size()];
        for (int i = 0; i < sortedCards.size(); i++) {
            values[i] = sortedCards.get(i).value();
        }
        return values;
    }

    // not currently in use
    public boolean equals(Hand hand) {
        if (cards.size() != hand.getCards().size()) {
            return false;
        }

        for (Card card : hand.getCards()) {
            if (!cards.contains(card)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        String handString = "";
        for (Card card : cards) {
            handString += card.toString() + " ";
        }

        if (handString.length() > 0) {
            handString = handString.substring(0, handString.length() - 1);
        }

        return handString;
    }

    @Override
    public int compareTo(Hand hand) {
        HandRank thisHandRank = HandRank.getHandRank(this);
        HandRank otherHandRank = HandRank.getHandRank(hand);
        if (thisHandRank != otherHandRank) {
            return thisHandRank.compareTo(otherHandRank);
        } else {
            return HandRank.compareHandRank(thisHandRank, this, hand);
        }
    }

    private List<Card> copyCards() {
        List<Card> copy = new ArrayList<Card>();
        for (Card card : cards) {
            copy.add(card);
        }
        return copy;
    }

    public static Hand parse(String text) {
        Hand hand = new Hand();
        for (String card : text.split(" ")) {
            hand.addCard(Card.parse(card));
        }
        return hand;
    }
}