package hannahschroeder.texasholdem;

import java.util.Arrays;
import java.util.List;

public enum HandRank {
    HIGHCARD,
    PAIR,
    TWOPAIR,
    THREEOFAKIND,
    STRAIGHT,
    FLUSH,
    FULLHOUSE,
    FOUROFAKIND,
    STRAIGHTFLUSH,
    ROYALFLUSH;

    public static String handRankString(Hand hand) {
        HandRank handRank = getHandRank(hand);

        switch (handRank) {
            case HIGHCARD: return String.format("high card %s", hand.highCard().rank().toString());
            case PAIR: return String.format("pair of %s's", HandRank.getNKindRank(hand, 2).toString());
            case TWOPAIR: return String.format("two pair, %s's and %s's", HandRank.getNKindRank(hand, 2).toString(), HandRank.getLowPairRank(hand).toString());
            case THREEOFAKIND: return String.format("three of a kind, %s's", HandRank.getNKindRank(hand, 3).toString());
            case STRAIGHT: return String.format("straight, %s high", hand.highCard().rank().toString());
            case FLUSH: return String.format("flush, %s high", hand.highCard().rank().toString());
            case FULLHOUSE: return String.format("full house, %s's over %s's", HandRank.getNKindRank(hand, 3).toString(), HandRank.getNKindRank(hand, 2).toString());
            case FOUROFAKIND: return String.format("four of a kind, %s's", HandRank.getNKindRank(hand, 4).toString());
            case STRAIGHTFLUSH: return String.format("straight flush, %s high", hand.highCard().rank().toString());
            case ROYALFLUSH: return String.format("royal flush");
            default: return "could not ascertain hand value";
        }
    }

    public static HandRank getHandRank(Hand hand) {
        if (hand.size() != Hand.FIVE_CARD_HAND) {
            throw new RuntimeException(String.format("Invalid hand size; expected hand of size 5, got size %d.", hand.size()));
        }

        if (isRoyalFlush(hand)) {
            return ROYALFLUSH;
        } else if (isStraightFlush(hand)) {
            return STRAIGHTFLUSH;
        } else if (isFourOfAKind(hand)) {
            return FOUROFAKIND;
        } else if (isFullHouse(hand)) {
            return FULLHOUSE;
        } else if (isFlush(hand)) {
            return FLUSH;
        } else if (isStraight(hand)) {
            return STRAIGHT;
        } else if (hasThreeOfAKind(hand)) {
            return THREEOFAKIND;
        } else if (isTwoPair(hand)) {
            return TWOPAIR;
        } else if (hasPair(hand)) {
            return PAIR;
        } else {
            return HIGHCARD;
        }
    }

    /**
     * comparison method for when hands are the same Rank
     * @param handRank
     * @param hand1
     * @param hand2
     * @return a positive number if hand1 outranks hand2, and a negative number
     * if hand2 outranks hand 1. Zero means a tie.
     */
    public static int compareHandRank(HandRank handRank, Hand hand1, Hand hand2) {
        switch (handRank) {
            case ROYALFLUSH: return 0;

            case STRAIGHTFLUSH: return compareStraight(hand1, hand2);

            case FOUROFAKIND: return compareFourOfAKind(hand1, hand2);

            case FULLHOUSE: return compareThreeOfAKind(hand1, hand2);

            case FLUSH: return compareHighCard(hand1, hand2);

            case STRAIGHT: return compareStraight(hand1, hand2);

            case THREEOFAKIND: return compareThreeOfAKind(hand1, hand2);

            case TWOPAIR: return compareTwoPair(hand1, hand2);

            case PAIR: return comparePair(hand1, hand2);

            case HIGHCARD: return compareHighCard(hand1, hand2);

            default: return 0;
        }
    }

    private static boolean isRoyalFlush(Hand hand) {
        return hand.highCard().rank() == Rank.ACE && isStraightFlush(hand);
    }

    private static boolean isStraightFlush(Hand hand) {
        return isFlush(hand) && isStraight(hand);
    }

    private static boolean isFourOfAKind(Hand hand) {
        int[] rankCount = getRankCount(hand);
        for (int i = 0; i < rankCount.length; i++) {
            if (rankCount[i] == 4) {
                return true;
            }
        }

        return false;
    }

    private static boolean isFullHouse(Hand hand) {
        return hasThreeOfAKind(hand) && hasPair(hand);
    }

    private static boolean isFlush(Hand hand) {
        for (Suit suit : Suit.values()) {
            int count = 0;
            for (Card card : hand.getCards()) {
                if (card.suit() == suit) {
                    count++;
                }
            }
            if (count == Hand.FIVE_CARD_HAND) {
                return true;
            }
        }

        return false;
    }

    private static boolean isStraight(Hand hand) {
        int[] values = hand.getSortedRankValues();

        int[] lowStraight = {14, 5, 4, 3, 2};
        if (Arrays.equals(values, lowStraight)) {
            return true;
        }

        int value = values[0];
        for (int i = 1; i < Hand.FIVE_CARD_HAND; i++) {
            if (values[i] != value - 1) {
                return false;
            }
            value = values[i];
        }

        return true;
    }

    private static boolean hasThreeOfAKind(Hand hand) {
        int[] rankCount = getRankCount(hand);
        for (int i = 0; i < rankCount.length; i++) {
            if (rankCount[i] == 3) {
                return true;
            }
        }

        return false;
    }

    private static boolean isTwoPair(Hand hand) {
        int pairs = 0;
        int[] rankCount = getRankCount(hand);
        for (int i = 0; i < rankCount.length; i++) {
            if (rankCount[i] == 2) {
                pairs++;
            }
        }

        if (pairs == 2) {
            return true;
        }
        return false;
    }

    private static boolean hasPair(Hand hand) {
        int[] rankCount = getRankCount(hand);
        for (int i = 0; i < rankCount.length; i++) {
            if (rankCount[i] == 2) {
                return true;
            }
        }

        return false;
    }

    private static int compareFourOfAKind(Hand hand1, Hand hand2) {
        int hand1SetValue = getNKindRankValue(hand1, 4);
        int hand2SetValue = getNKindRankValue(hand2, 4);

        if (hand1SetValue != hand2SetValue) {
            return hand1SetValue - hand2SetValue;
        } else {
            Hand hand1Kickers = getKickers(hand1);
            Hand hand2Kickers = getKickers(hand2);
            return compareHighCard(hand1Kickers, hand2Kickers);
        }
    }

    private static int compareStraight(Hand hand1, Hand hand2) {
        int[] lowAceStraight = {14, 5, 4, 3, 2};
        int[] lowAceStraightFixed = {5, 4, 3, 2, 1};

        int[] hand1Values = hand1.getSortedRankValues();
        int[] hand2Values = hand2.getSortedRankValues();

        if (Arrays.equals(hand1.getSortedRankValues(), lowAceStraight)) {
            hand1Values = lowAceStraightFixed;
        }
        if (Arrays.equals(hand2.getSortedRankValues(), lowAceStraight)) {
            hand2Values = lowAceStraightFixed;
        }
        
        return hand1Values[0] - hand2Values[0];
    }

    private static int compareThreeOfAKind(Hand hand1, Hand hand2) {
        int hand1SetValue = getNKindRankValue(hand1, 3);
        int hand2SetValue = getNKindRankValue(hand2, 3);

        if (hand1SetValue != hand2SetValue) {
            return hand1SetValue - hand2SetValue;
        } else {
            Hand hand1Kickers = getKickers(hand1);
            Hand hand2Kickers = getKickers(hand2);
            return compareHighCard(hand1Kickers, hand2Kickers);
        }
    }

    private static int compareTwoPair(Hand hand1, Hand hand2) {
        int hand1SetValue = getNKindRankValue(hand1, 2);
        int hand2SetValue = getNKindRankValue(hand2, 2);

        if (hand1SetValue != hand2SetValue) {
            return hand1SetValue - hand2SetValue;
        } else {
            Hand newHand1 = removeHighPair(hand1);
            Hand newHand2 = removeHighPair(hand2);

            hand1SetValue = getNKindRankValue(newHand1, 2);
            hand2SetValue = getNKindRankValue(newHand2, 2);

            if (hand1SetValue != hand2SetValue) {
                return hand1SetValue - hand2SetValue;
            } else {
                Hand hand1Kickers = getKickers(newHand1);
                Hand hand2Kickers = getKickers(newHand2);
                return compareHighCard(hand1Kickers, hand2Kickers);
            }
        }
    }

    private static Hand removeHighPair(Hand hand) {
        int highPairValue = getNKindRankValue(hand, 2);
        List<Card> cards = hand.getCards();
        Hand newHand = new Hand();
        for (Card card : cards) {
            if (card.rank().value() != highPairValue) {
                newHand.addCard(card);
            }
        }
        return newHand;
    }

    private static int comparePair(Hand hand1, Hand hand2) {
        int hand1SetValue = getNKindRankValue(hand1, 2);
        int hand2SetValue = getNKindRankValue(hand2, 2);

        if (hand1SetValue != hand2SetValue) {
            return hand1SetValue - hand2SetValue;
        } else {
            Hand hand1Kickers = getKickers(hand1);
            Hand hand2Kickers = getKickers(hand2);
            return compareHighCard(hand1Kickers, hand2Kickers);
        }
    }

    private static int compareHighCard(Hand hand1, Hand hand2) {
        int[] hand1Values = hand1.getSortedRankValues();
        int[] hand2Values = hand2.getSortedRankValues();
        
        return compareHighCard(hand1Values, hand2Values);
    }

    private static int compareHighCard(int[] hand1Values, int[] hand2Values) {
        for (int i = 0; i < hand1Values.length; i++) {
            if (hand1Values[i] != hand2Values[i]) {
                return hand1Values[i] - hand2Values[i];
            }
        }
        return 0;
    }

    private static Hand getKickers(Hand hand) {
        int[] rankCount = getRankCount(hand);
        Hand newHand = new Hand();
        List<Card> cards = hand.getCards();
        for (Card card : cards) {
            int rankValue = card.rank().value();
            if (rankCount[rankValue - 2] == 1) {
                newHand.addCard(card);
            }
        }

        return newHand;
    }

    private static int getNKindRankValue(Hand hand, int count) {
        int cardRankValue = 0;
        int[] rankCount = getRankCount(hand);
        for (int i = 12; i >= 0; i--) {
            if (rankCount[i] == count) {
                cardRankValue = i + 2;
            }
        }

        return cardRankValue;
    }

    private static Rank getNKindRank(Hand hand, int count) {
        int rankValue = getNKindRankValue(hand, count);
        return Rank.byValue(rankValue);
    }

    private static Rank getLowPairRank(Hand hand) {
        Hand newHand = removeHighPair(hand);
        return getNKindRank(newHand, 2);
    }

    private static int[] getRankCount(Hand hand) {
        int[] rankCount = new int[13];
        List<Card> cards = hand.getCards();
        for (Card card : cards) {
            int rankValue = card.rank().value();
            rankCount[rankValue - 2]++;
        }
        return rankCount;
    }
}