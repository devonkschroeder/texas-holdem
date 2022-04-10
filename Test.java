class Test {
    public static void main(String[] args) {

        Hand royalFlushHearts = new Hand();
        royalFlushHearts.addCard(new Card(Rank.TEN, Suit.HEARTS));
        royalFlushHearts.addCard(new Card(Rank.JACK, Suit.HEARTS));
        royalFlushHearts.addCard(new Card(Rank.QUEEN, Suit.HEARTS));
        royalFlushHearts.addCard(new Card(Rank.KING, Suit.HEARTS));
        royalFlushHearts.addCard(new Card(Rank.ACE, Suit.HEARTS));

        Hand royalFlushSpades = new Hand();
        royalFlushSpades.addCard(new Card(Rank.TEN, Suit.SPADES));
        royalFlushSpades.addCard(new Card(Rank.JACK, Suit.SPADES));
        royalFlushSpades.addCard(new Card(Rank.QUEEN, Suit.SPADES));
        royalFlushSpades.addCard(new Card(Rank.KING, Suit.SPADES));
        royalFlushSpades.addCard(new Card(Rank.ACE, Suit.SPADES));

        Hand straightFlushJackHigh = new Hand();
        straightFlushJackHigh.addCard(new Card(Rank.SEVEN, Suit.HEARTS));
        straightFlushJackHigh.addCard(new Card(Rank.EIGHT, Suit.HEARTS));
        straightFlushJackHigh.addCard(new Card(Rank.NINE, Suit.HEARTS));
        straightFlushJackHigh.addCard(new Card(Rank.TEN, Suit.HEARTS));
        straightFlushJackHigh.addCard(new Card(Rank.JACK, Suit.HEARTS));

        Hand straightFlushSevenHigh = new Hand();
        straightFlushSevenHigh.addCard(new Card(Rank.THREE, Suit.HEARTS));
        straightFlushSevenHigh.addCard(new Card(Rank.FOUR, Suit.HEARTS));
        straightFlushSevenHigh.addCard(new Card(Rank.FIVE, Suit.HEARTS));
        straightFlushSevenHigh.addCard(new Card(Rank.SIX, Suit.HEARTS));
        straightFlushSevenHigh.addCard(new Card(Rank.SEVEN, Suit.HEARTS));

        Hand fourOfAKindKingHigh = new Hand();
        fourOfAKindKingHigh.addCard(new Card(Rank.KING, Suit.SPADES));
        fourOfAKindKingHigh.addCard(new Card(Rank.KING, Suit.HEARTS));
        fourOfAKindKingHigh.addCard(new Card(Rank.KING, Suit.DIAMONDS));
        fourOfAKindKingHigh.addCard(new Card(Rank.KING, Suit.CLUBS));
        fourOfAKindKingHigh.addCard(new Card(Rank.FIVE, Suit.SPADES));

        Hand fourOfAKindSixHigh = new Hand();
        fourOfAKindSixHigh.addCard(new Card(Rank.SIX, Suit.SPADES));
        fourOfAKindSixHigh.addCard(new Card(Rank.SIX, Suit.HEARTS));
        fourOfAKindSixHigh.addCard(new Card(Rank.SIX, Suit.DIAMONDS));
        fourOfAKindSixHigh.addCard(new Card(Rank.SIX, Suit.CLUBS));
        fourOfAKindSixHigh.addCard(new Card(Rank.FIVE, Suit.HEARTS));

        Hand fullHouseKings = new Hand();
        fullHouseKings.addCard(new Card(Rank.KING, Suit.SPADES));
        fullHouseKings.addCard(new Card(Rank.KING, Suit.HEARTS));
        fullHouseKings.addCard(new Card(Rank.KING, Suit.DIAMONDS));
        fullHouseKings.addCard(new Card(Rank.EIGHT, Suit.SPADES));
        fullHouseKings.addCard(new Card(Rank.EIGHT, Suit.CLUBS));

        Hand fullHouseJacks = new Hand();
        fullHouseJacks.addCard(new Card(Rank.JACK, Suit.SPADES));
        fullHouseJacks.addCard(new Card(Rank.JACK, Suit.HEARTS));
        fullHouseJacks.addCard(new Card(Rank.JACK, Suit.DIAMONDS));
        fullHouseJacks.addCard(new Card(Rank.TWO, Suit.SPADES));
        fullHouseJacks.addCard(new Card(Rank.TWO, Suit.CLUBS));

        Hand fullHouseJacks2 = new Hand();
        fullHouseJacks2.addCard(new Card(Rank.JACK, Suit.SPADES));
        fullHouseJacks2.addCard(new Card(Rank.JACK, Suit.HEARTS));
        fullHouseJacks2.addCard(new Card(Rank.JACK, Suit.CLUBS));
        fullHouseJacks2.addCard(new Card(Rank.TWO, Suit.SPADES));
        fullHouseJacks2.addCard(new Card(Rank.TWO, Suit.CLUBS));

        Hand flushKingHigh = new Hand();
        flushKingHigh.addCard(new Card(Rank.KING, Suit.SPADES));
        flushKingHigh.addCard(new Card(Rank.QUEEN, Suit.SPADES));
        flushKingHigh.addCard(new Card(Rank.NINE, Suit.SPADES));
        flushKingHigh.addCard(new Card(Rank.EIGHT, Suit.SPADES));
        flushKingHigh.addCard(new Card(Rank.TWO, Suit.SPADES));

        Hand flushJackHigh = new Hand();
        flushJackHigh.addCard(new Card(Rank.JACK, Suit.SPADES));
        flushJackHigh.addCard(new Card(Rank.TEN, Suit.SPADES));
        flushJackHigh.addCard(new Card(Rank.SEVEN, Suit.SPADES));
        flushJackHigh.addCard(new Card(Rank.SIX, Suit.SPADES));
        flushJackHigh.addCard(new Card(Rank.THREE, Suit.SPADES));

        Hand straightTenHigh = new Hand();
        straightTenHigh.addCard(new Card(Rank.SIX, Suit.HEARTS));
        straightTenHigh.addCard(new Card(Rank.SEVEN, Suit.HEARTS));
        straightTenHigh.addCard(new Card(Rank.EIGHT, Suit.DIAMONDS));
        straightTenHigh.addCard(new Card(Rank.NINE, Suit.CLUBS));
        straightTenHigh.addCard(new Card(Rank.TEN, Suit.SPADES));

        Hand straightSevenHighSpades = new Hand();
        straightSevenHighSpades.addCard(new Card(Rank.THREE, Suit.CLUBS));
        straightSevenHighSpades.addCard(new Card(Rank.FOUR, Suit.HEARTS));
        straightSevenHighSpades.addCard(new Card(Rank.FIVE, Suit.DIAMONDS));
        straightSevenHighSpades.addCard(new Card(Rank.SIX, Suit.CLUBS));
        straightSevenHighSpades.addCard(new Card(Rank.SEVEN, Suit.SPADES));

        Hand straightSevenHighClubs = new Hand();
        straightSevenHighClubs.addCard(new Card(Rank.THREE, Suit.HEARTS));
        straightSevenHighClubs.addCard(new Card(Rank.FOUR, Suit.CLUBS));
        straightSevenHighClubs.addCard(new Card(Rank.FIVE, Suit.SPADES));
        straightSevenHighClubs.addCard(new Card(Rank.SIX, Suit.DIAMONDS));
        straightSevenHighClubs.addCard(new Card(Rank.SEVEN, Suit.CLUBS));

        Hand threeOfAKindAces = new Hand();
        threeOfAKindAces.addCard(new Card(Rank.ACE, Suit.SPADES));
        threeOfAKindAces.addCard(new Card(Rank.ACE, Suit.HEARTS));
        threeOfAKindAces.addCard(new Card(Rank.ACE, Suit.DIAMONDS));
        threeOfAKindAces.addCard(new Card(Rank.SIX, Suit.SPADES));
        threeOfAKindAces.addCard(new Card(Rank.SEVEN, Suit.CLUBS));

        Hand threeOfAKindKings = new Hand();
        threeOfAKindKings.addCard(new Card(Rank.KING, Suit.SPADES));
        threeOfAKindKings.addCard(new Card(Rank.KING, Suit.HEARTS));
        threeOfAKindKings.addCard(new Card(Rank.KING, Suit.DIAMONDS));
        threeOfAKindKings.addCard(new Card(Rank.EIGHT, Suit.SPADES));
        threeOfAKindKings.addCard(new Card(Rank.FIVE, Suit.CLUBS));

        Hand threeOfAKindKings2 = new Hand();
        threeOfAKindKings2.addCard(new Card(Rank.KING, Suit.SPADES));
        threeOfAKindKings2.addCard(new Card(Rank.KING, Suit.HEARTS));
        threeOfAKindKings2.addCard(new Card(Rank.KING, Suit.CLUBS));
        threeOfAKindKings2.addCard(new Card(Rank.EIGHT, Suit.HEARTS));
        threeOfAKindKings2.addCard(new Card(Rank.FIVE, Suit.DIAMONDS));

        Hand twoPairsKingEightSix = new Hand();
        twoPairsKingEightSix.addCard(new Card(Rank.KING, Suit.CLUBS));
        twoPairsKingEightSix.addCard(new Card(Rank.KING, Suit.DIAMONDS));
        twoPairsKingEightSix.addCard(new Card(Rank.EIGHT, Suit.HEARTS));
        twoPairsKingEightSix.addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        twoPairsKingEightSix.addCard(new Card(Rank.SIX, Suit.CLUBS));

        Hand twoPairsKingEightFive = new Hand();
        twoPairsKingEightFive.addCard(new Card(Rank.KING, Suit.SPADES));
        twoPairsKingEightFive.addCard(new Card(Rank.KING, Suit.HEARTS));
        twoPairsKingEightFive.addCard(new Card(Rank.EIGHT, Suit.DIAMONDS));
        twoPairsKingEightFive.addCard(new Card(Rank.EIGHT, Suit.SPADES));
        twoPairsKingEightFive.addCard(new Card(Rank.FIVE, Suit.CLUBS));

        Hand twoPairsKingEightFive2 = new Hand();
        twoPairsKingEightFive2.addCard(new Card(Rank.KING, Suit.DIAMONDS));
        twoPairsKingEightFive2.addCard(new Card(Rank.KING, Suit.CLUBS));
        twoPairsKingEightFive2.addCard(new Card(Rank.EIGHT, Suit.HEARTS));
        twoPairsKingEightFive2.addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        twoPairsKingEightFive2.addCard(new Card(Rank.FIVE, Suit.SPADES));

        Hand twoPairsKingSevenThree = new Hand();
        twoPairsKingSevenThree.addCard(new Card(Rank.KING, Suit.SPADES));
        twoPairsKingSevenThree.addCard(new Card(Rank.KING, Suit.HEARTS));
        twoPairsKingSevenThree.addCard(new Card(Rank.SEVEN, Suit.DIAMONDS));
        twoPairsKingSevenThree.addCard(new Card(Rank.SEVEN, Suit.SPADES));
        twoPairsKingSevenThree.addCard(new Card(Rank.THREE, Suit.CLUBS));

        Hand pairKingQueenSevenThree = new Hand();
        pairKingQueenSevenThree.addCard(new Card(Rank.KING, Suit.SPADES));
        pairKingQueenSevenThree.addCard(new Card(Rank.KING, Suit.HEARTS));
        pairKingQueenSevenThree.addCard(new Card(Rank.QUEEN, Suit.DIAMONDS));
        pairKingQueenSevenThree.addCard(new Card(Rank.SEVEN, Suit.CLUBS));
        pairKingQueenSevenThree.addCard(new Card(Rank.THREE, Suit.CLUBS));

        Hand pairKingQueenSevenFour = new Hand();
        pairKingQueenSevenFour.addCard(new Card(Rank.KING, Suit.SPADES));
        pairKingQueenSevenFour.addCard(new Card(Rank.KING, Suit.HEARTS));
        pairKingQueenSevenFour.addCard(new Card(Rank.QUEEN, Suit.DIAMONDS));
        pairKingQueenSevenFour.addCard(new Card(Rank.SEVEN, Suit.CLUBS));
        pairKingQueenSevenFour.addCard(new Card(Rank.FOUR, Suit.CLUBS));

        Hand pairKingQueenFiveFour = new Hand();
        pairKingQueenFiveFour.addCard(new Card(Rank.KING, Suit.SPADES));
        pairKingQueenFiveFour.addCard(new Card(Rank.KING, Suit.HEARTS));
        pairKingQueenFiveFour.addCard(new Card(Rank.QUEEN, Suit.DIAMONDS));
        pairKingQueenFiveFour.addCard(new Card(Rank.FIVE, Suit.CLUBS));
        pairKingQueenFiveFour.addCard(new Card(Rank.FOUR, Suit.CLUBS));

        Hand pairKingEightFiveTwo = new Hand();
        pairKingEightFiveTwo.addCard(new Card(Rank.KING, Suit.SPADES));
        pairKingEightFiveTwo.addCard(new Card(Rank.KING, Suit.HEARTS));
        pairKingEightFiveTwo.addCard(new Card(Rank.EIGHT, Suit.DIAMONDS));
        pairKingEightFiveTwo.addCard(new Card(Rank.FIVE, Suit.CLUBS));
        pairKingEightFiveTwo.addCard(new Card(Rank.TWO, Suit.CLUBS));

        Hand pairQueenEightFiveFour = new Hand();
        pairQueenEightFiveFour.addCard(new Card(Rank.QUEEN, Suit.SPADES));
        pairQueenEightFiveFour.addCard(new Card(Rank.QUEEN, Suit.HEARTS));
        pairQueenEightFiveFour.addCard(new Card(Rank.EIGHT, Suit.DIAMONDS));
        pairQueenEightFiveFour.addCard(new Card(Rank.FIVE, Suit.CLUBS));
        pairQueenEightFiveFour.addCard(new Card(Rank.FOUR, Suit.CLUBS));

        Hand pairQueenEightFiveFour2 = new Hand();
        pairQueenEightFiveFour2.addCard(new Card(Rank.QUEEN, Suit.CLUBS));
        pairQueenEightFiveFour2.addCard(new Card(Rank.QUEEN, Suit.DIAMONDS));
        pairQueenEightFiveFour2.addCard(new Card(Rank.EIGHT, Suit.SPADES));
        pairQueenEightFiveFour2.addCard(new Card(Rank.FIVE, Suit.HEARTS));
        pairQueenEightFiveFour2.addCard(new Card(Rank.FOUR, Suit.HEARTS));

        Hand highCardAceKingQueenJackNine = new Hand();
        highCardAceKingQueenJackNine.addCard(new Card(Rank.ACE, Suit.CLUBS));
        highCardAceKingQueenJackNine.addCard(new Card(Rank.KING, Suit.HEARTS));
        highCardAceKingQueenJackNine.addCard(new Card(Rank.QUEEN, Suit.DIAMONDS));
        highCardAceKingQueenJackNine.addCard(new Card(Rank.JACK, Suit.CLUBS));
        highCardAceKingQueenJackNine.addCard(new Card(Rank.NINE, Suit.SPADES));

        Hand highCardAceKingQueenJackEight = new Hand();
        highCardAceKingQueenJackEight.addCard(new Card(Rank.ACE, Suit.CLUBS));
        highCardAceKingQueenJackEight.addCard(new Card(Rank.KING, Suit.HEARTS));
        highCardAceKingQueenJackEight.addCard(new Card(Rank.QUEEN, Suit.DIAMONDS));
        highCardAceKingQueenJackEight.addCard(new Card(Rank.JACK, Suit.CLUBS));
        highCardAceKingQueenJackEight.addCard(new Card(Rank.EIGHT, Suit.SPADES));

        Hand highCardAceKingQueenTenSix = new Hand();
        highCardAceKingQueenTenSix.addCard(new Card(Rank.ACE, Suit.CLUBS));
        highCardAceKingQueenTenSix.addCard(new Card(Rank.KING, Suit.HEARTS));
        highCardAceKingQueenTenSix.addCard(new Card(Rank.QUEEN, Suit.DIAMONDS));
        highCardAceKingQueenTenSix.addCard(new Card(Rank.TEN, Suit.CLUBS));
        highCardAceKingQueenTenSix.addCard(new Card(Rank.SIX, Suit.SPADES));

        Hand highCardAceKingJackNineTwo = new Hand();
        highCardAceKingJackNineTwo.addCard(new Card(Rank.ACE, Suit.CLUBS));
        highCardAceKingJackNineTwo.addCard(new Card(Rank.KING, Suit.HEARTS));
        highCardAceKingJackNineTwo.addCard(new Card(Rank.JACK, Suit.DIAMONDS));
        highCardAceKingJackNineTwo.addCard(new Card(Rank.NINE, Suit.CLUBS));
        highCardAceKingJackNineTwo.addCard(new Card(Rank.TWO, Suit.SPADES));

        Hand highCardAceJackSevenFourTwo = new Hand();
        highCardAceJackSevenFourTwo.addCard(new Card(Rank.ACE, Suit.CLUBS));
        highCardAceJackSevenFourTwo.addCard(new Card(Rank.JACK, Suit.HEARTS));
        highCardAceJackSevenFourTwo.addCard(new Card(Rank.SEVEN, Suit.DIAMONDS));
        highCardAceJackSevenFourTwo.addCard(new Card(Rank.FOUR, Suit.CLUBS));
        highCardAceJackSevenFourTwo.addCard(new Card(Rank.TWO, Suit.SPADES));

        Hand highCardQueenJackTenSixThree = new Hand();
        highCardQueenJackTenSixThree.addCard(new Card(Rank.QUEEN, Suit.CLUBS));
        highCardQueenJackTenSixThree.addCard(new Card(Rank.JACK, Suit.HEARTS));
        highCardQueenJackTenSixThree.addCard(new Card(Rank.TEN, Suit.DIAMONDS));
        highCardQueenJackTenSixThree.addCard(new Card(Rank.SIX, Suit.CLUBS));
        highCardQueenJackTenSixThree.addCard(new Card(Rank.THREE, Suit.SPADES));

        Hand highCardQueenJackTenSixThree2 = new Hand();
        highCardQueenJackTenSixThree2.addCard(new Card(Rank.QUEEN, Suit.HEARTS));
        highCardQueenJackTenSixThree2.addCard(new Card(Rank.JACK, Suit.SPADES));
        highCardQueenJackTenSixThree2.addCard(new Card(Rank.TEN, Suit.CLUBS));
        highCardQueenJackTenSixThree2.addCard(new Card(Rank.SIX, Suit.DIAMONDS));
        highCardQueenJackTenSixThree2.addCard(new Card(Rank.THREE, Suit.CLUBS));

        Hand[] hands = {
            royalFlushHearts,
            royalFlushSpades,
            straightFlushJackHigh,
            straightFlushSevenHigh,
            fourOfAKindKingHigh,
            fourOfAKindSixHigh,
            fullHouseKings,
            fullHouseJacks,
            fullHouseJacks2,
            flushKingHigh,
            flushJackHigh,
            straightTenHigh,
            straightSevenHighClubs,
            straightSevenHighSpades,
            threeOfAKindAces,
            threeOfAKindKings,
            threeOfAKindKings2,
            twoPairsKingEightSix,
            twoPairsKingEightFive,
            twoPairsKingEightFive2,
            twoPairsKingSevenThree,
            pairKingQueenSevenFour,
            pairKingQueenSevenThree,
            pairKingQueenFiveFour,
            pairQueenEightFiveFour,
            pairQueenEightFiveFour2,
            highCardAceKingQueenJackNine,
            highCardAceKingQueenJackEight,
            highCardAceKingQueenTenSix,
            highCardAceKingJackNineTwo,
            highCardAceJackSevenFourTwo,
            highCardQueenJackTenSixThree,
            highCardQueenJackTenSixThree2
        };

        int[] scores = {
            26,
            26,
            25,
            24,
            23,
            22,
            21,
            20,
            20,
            19,
            18,
            17,
            16,
            16,
            15,
            14,
            14,
            13,
            12,
            12,
            11,
            10,
            9,
            8,
            7,
            7,
            6,
            5,
            4,
            3,
            2,
            1,
            1
        };

        TestHarness tester = new TestHarness();
        String draw = "draw";

        for (int i = 0; i < hands.length - 1; i++) {

            Hand player1 = hands[i];

            for (int j = i + 1; j < hands.length; j++) {
                Hand player2 = hands[j];

                String expectedWinner = null;
                String actualWinner = null;

                if (scores[i] == scores[j]) {
                    expectedWinner = draw;
                } else if (scores[i] > scores[j]) {
                    expectedWinner = player1.toString();
                } else {
                    expectedWinner = player2.toString();
                }

                if (player1.compareTo(player2) == 0) {
                    actualWinner = draw;
                } else if (player1.compareTo(player2) > 0) {
                    actualWinner = player1.toString();
                } else {
                    actualWinner = player2.toString();
                }

                // System.out.printf("Player 1: %s (%s)%nPlayer 2: %s (%s)%n", player1.toString(), HandValue.handValueString(player1), player2.toString(), HandValue.handValueString(player1));
                tester.assertEqual("Hand comparison", expectedWinner, actualWinner, "wrong winner");
            }

            /*

            HandValue handValue = HandValue.getHandValue(hands[i]);

            switch (handValue) {
                case ROYALFLUSH:
                    System.out.println("Player has a royal flush.");
                    break;
                case STRAIGHTFLUSH:
                    System.out.println("Player has a straight flush.");
                    break;
                case FOUROFAKIND:
                    System.out.println("Player has four of a kind.");
                    break;
                case FULLHOUSE:
                    System.out.println("Player has a full house.");
                    break;
                case FLUSH:
                    System.out.println("Player has a flush.");
                    break;
                case STRAIGHT:
                    System.out.println("Player has a straight.");
                    break;
                case THREEOFAKIND:
                    System.out.println("Player has three of a kind.");
                    break;
                case TWOPAIRS:
                    System.out.println("Player has two pairs.");
                    break;
                case PAIR:
                    System.out.println("Player has a pair.");
                    break;
                case HIGHCARD:
                    System.out.printf("Player has high card %s.%n", hands[i].highCard().rank().getSymbol());
                    break;
            }
            System.out.println();

            */
        }

        System.out.println(tester.getEvaluationSummary());
        
    }
}