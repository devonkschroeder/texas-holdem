package hannahschroeder.texasholdem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Player {
    private static final int[][] combinations = {
        {0, 1, 2, 3, 4},
        {0, 1, 2, 3, 5},
        {0, 1, 2, 3, 6},
        {0, 1, 2, 4, 5},
        {0, 1, 2, 4, 6},
        {0, 1, 2, 5, 6},
        {0, 1, 3, 4, 5},
        {0, 1, 3, 4, 6},
        {0, 1, 3, 5, 6},
        {0, 1, 4, 5, 6},
        {0, 2, 3, 4, 5},
        {0, 2, 3, 4, 6},
        {0, 2, 3, 5, 6},
        {0, 2, 4, 5, 6},
        {0, 3, 4, 5, 6},
        {1, 2, 3, 4, 5},
        {1, 2, 3, 4, 6},
        {1, 2, 3, 5, 6},
        {1, 2, 4, 5, 6},
        {1, 3, 4, 5, 6},
        {2, 3, 4, 5, 6}
    };
    private String name;
    private Pot stack;
    private Hand pocket;
    private Hand finalHand;
    private boolean isActive;
    private boolean mustReveal;

    public Player(String name, int startAmount) {
        this.name = name;
        stack = new Pot(startAmount);
        pocket = new Hand();
        mustReveal = true;
    }

    public String getName() {
        return name;
    }

    public void receiveCard(Card card) {
        pocket.addCard(card);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive() {
        isActive = true;
    }

    public int stackValue() {
        return stack.getTotal();
    }

    public void removeFromStack(int value) {
        stack.removeValue(value);
    }

    public void addToStack(int value) {
        stack.addValue(value);
    }

    public Hand getPrivateHand() {
        return pocket;
    }

    public void determineFinalHand(Hand tableHand) {
        List<Card> sevenCards = new ArrayList<>();
        sevenCards.addAll(pocket.getCards());
        sevenCards.addAll(tableHand.getCards());

        if (sevenCards.size() != 7) {
            throw new RuntimeException(String.format("Could not compute final hand; expected 7 cards, found %d.", sevenCards.size()));
        }

        List<Hand> allPossibleHands = new ArrayList<Hand>();
        for (int i = 0; i < combinations.length; i++) {
            Hand newHand = new Hand();
            int[] indices = combinations[i];
            for (int j = 0; j < 5; j++) {
                newHand.addCard(sevenCards.get(indices[j]));
            }
            allPossibleHands.add(newHand);
        }

        Collections.sort(allPossibleHands, Collections.reverseOrder());

        finalHand = allPossibleHands.get(0);
    }

    public Hand getFinalHand() {
        return finalHand;
    }

    public void dontReveal() {
        mustReveal = false;
    }

    public void fold() {
        isActive = false;
        mustReveal = false;
    }

    public boolean mustReveal() {
        return mustReveal;
    }
}