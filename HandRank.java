import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            case TWOPAIR: return String.format("two pair, %s's and %s's", HandRank.getRankListByCount(hand, 2).get(0).toString(), HandRank.getRankListByCount(hand, 2).get(1).toString());
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
        } else if (isThreeOfAKind(hand)) {
            return THREEOFAKIND;
        } else if (isTwoPair(hand)) {
            return TWOPAIR;
        } else if (isPair(hand)) {
            return PAIR;
        } else {
            return HIGHCARD;
        }
    }

    /**
     * 
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
            for (Rank rank : Rank.values()) {
            int count = 0;
            for (Card card : hand.getCards()) {
                if (card.rank() == rank) {
                    count++;
                }
            }
            if (count == 4) {
                return true;
            }
        }

        return false;
    }

    private static boolean isFullHouse(Hand hand) {
        return isThreeOfAKind(hand) && isPair(hand);
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

        int[] lowStraight = {2, 3, 4, 5, 14};
        if (Arrays.equals(values, lowStraight)) {
            return true;
        }

        int value = values[0];
        for (int i = 1; i < Hand.FIVE_CARD_HAND; i++) {
            if (values[i] != value + 1) {
                return false;
            }
            value = values[i];
        }

        return true;
    }

    private static boolean isThreeOfAKind(Hand hand) {
        for (Rank rank : Rank.values()) {
            int count = 0;
            for (Card card : hand.getCards()) {
                if (card.rank() == rank) {
                    count++;
                }
            }
            if (count == 3) {
                return true;
            }
        }
        
        return false;
    }

    // TODO: Replace contents of isTwoPair and isPair
    //       with a function that counts pairs and use
    //       its result to decide if you have 0, 1 or
    //       2 pairs in a hand.
    //       (Consider also looking at the getNKindRank
    //       for this purpose, as it's already counting
    //       numbers of cards in hand)
    private static boolean isTwoPair(Hand hand) {
        int pairs = 0;
        for (Rank rank : Rank.values()) {
            int count = 0;
            for (Card card : hand.getCards()) {
                if (card.rank() == rank) {
                    count++;
                }
            }
            if (count == 2) {
                pairs++;
                if (pairs == 2) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isPair(Hand hand) {
        for (Rank rank : Rank.values()) {
            int count = 0;
            for (Card card : hand.getCards()) {
                if (card.rank() == rank) {
                    count++;
                }
            }
            if (count == 2) {
                return true;
            }
        }

        return false;
    }

    private static int compareFourOfAKind(Hand hand1, Hand hand2) {
        int hand1SetValue = getNKindRank(hand1, 4).value();
        int hand2SetValue = getNKindRank(hand2, 4).value();

        return hand1SetValue - hand2SetValue;
    }

    private static int compareStraight(Hand hand1, Hand hand2) {
        int[] lowAceStraight = {2, 3, 4, 5, 14};
        int[] lowAceStraightFixed = {1, 2, 3, 4, 5};

        int[] hand1Values = hand1.getSortedRankValues();
        int[] hand2Values = hand2.getSortedRankValues();

        if (Arrays.equals(hand1.getSortedRankValues(), lowAceStraight)) {
            hand1Values = lowAceStraightFixed;
        }
        if (Arrays.equals(hand2.getSortedRankValues(), lowAceStraight)) {
            hand2Values = lowAceStraightFixed;
        }
        
        return hand1Values[hand1Values.length - 1] - hand2Values[hand2Values.length - 1];
    }

    private static int compareThreeOfAKind(Hand hand1, Hand hand2) {
        int hand1SetValue = getNKindRank(hand1, 3).value();
        int hand2SetValue = getNKindRank(hand2, 3).value();

        return hand1SetValue - hand2SetValue;
    }

    private static int compareTwoPair(Hand hand1, Hand hand2) {
        List<Rank> hand1RankList = getRankListByCount(hand1, 2);
        Collections.sort(hand1RankList, Collections.reverseOrder());
        int hand1GreaterPair = hand1RankList.get(0).value();

        List<Rank> hand2RankList = getRankListByCount(hand2, 2);
        Collections.sort(hand2RankList, Collections.reverseOrder());
        int hand2GreaterPair = hand2RankList.get(0).value();

        if (hand1GreaterPair != hand2GreaterPair) {
            return hand1GreaterPair - hand2GreaterPair;
        }

        int hand1LesserPair = hand1RankList.get(1).value();
        int hand2LesserPair = hand2RankList.get(1).value();
        
        if (hand1LesserPair != hand2LesserPair) {
            return hand1LesserPair - hand2LesserPair;
        }

        int hand1Kicker = 0;
        int hand2Kicker = 0;

        for (int value : hand1.getSortedRankValues()) {
            if (value != hand1GreaterPair && value != hand1LesserPair) {
                hand1Kicker = value;
            }
        }

        for (int value : hand2.getSortedRankValues()) {
            if (value != hand2GreaterPair && value != hand2LesserPair) {
                hand2Kicker = value;
            }
        }
        
        return hand1Kicker - hand2Kicker;
    }

    private static int comparePair(Hand hand1, Hand hand2) {
        int hand1SetValue = getNKindRank(hand1, 2).value();
        int hand2SetValue = getNKindRank(hand2, 2).value();
        
        if (hand1SetValue != hand2SetValue) {
            return hand1SetValue - hand2SetValue;
        }

        int[] hand1Kickers = new int[3];
        int[] hand2Kickers = new int[3];

        int i = 0;
        for (int value : hand1.getSortedRankValues()) {
            if (value != hand1SetValue) {
                hand1Kickers[i] = value;
                i++;
            }
        }
        
        i = 0;
        for (int value : hand2.getSortedRankValues()) {
            if (value != hand2SetValue) {
                hand2Kickers[i] = value;
                i++;
            }
        }
        
        return compareHighCard(hand1Kickers, hand2Kickers);
    }

    private static int compareHighCard(Hand hand1, Hand hand2) {
        int[] hand1Values = hand1.getSortedRankValues();
        int[] hand2Values = hand2.getSortedRankValues();
        
        return compareHighCard(hand1Values, hand2Values);
    }

    private static int compareHighCard(int[] hand1Values, int[] hand2Values) {
        for (int i = hand1Values.length - 1; i >= 0; i--) {
            if (hand1Values[i] != hand2Values[i]) {
                return hand1Values[i] - hand2Values[i];
            }
        }
        return 0;
    }

    private static Rank getNKindRank(Hand hand, int count) {
        List<Rank> rankList = getRankListByCount(hand, count);
        return rankList.get(0);
    }

    private static List<Rank> getRankListByCount(Hand hand, int count) {
        HashMap<Rank, Integer> map = getRankCountMap(hand);
        List<Rank> ranks = new ArrayList<>();
        for (Map.Entry<Rank, Integer> entry : map.entrySet()) {
            if (entry.getValue() == count) {
                ranks.add(entry.getKey());
            }
        }

        Collections.sort(ranks, Collections.reverseOrder());

        return ranks;
    }

    private static HashMap<Rank, Integer> getRankCountMap(Hand hand) {
        HashMap<Rank, Integer> rankCountMap = new HashMap<>();

        for (Card card : hand.getCards()) {
            if (rankCountMap.containsKey(card.rank())) {
                int oldCount = rankCountMap.get(card.rank());
                rankCountMap.put(card.rank(), oldCount + 1);
            } else {
                rankCountMap.put(card.rank(), 1);
            }
        }
        
        return rankCountMap;
    }
}