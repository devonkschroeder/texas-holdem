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
            handString += card.toString() +  " ";
        }

        handString = handString.substring(0, handString.length() - 1);

        return handString;
    }

    @Override
    public int compareTo(Hand hand) {
        HandRank thisHandValue = HandRank.getHandRank(this);
        HandRank otherHandValue = HandRank.getHandRank(hand);
        if (thisHandValue != otherHandValue) {
            return thisHandValue.compareTo(otherHandValue);
        } else {
            return HandRank.compareHandRank(thisHandValue, this, hand);
        }
    }

    private List<Card> copyCards() {
        List<Card> copy = new ArrayList<Card>();
        for (Card card : cards) {
            copy.add(card);
        }
        return copy;
    }
}