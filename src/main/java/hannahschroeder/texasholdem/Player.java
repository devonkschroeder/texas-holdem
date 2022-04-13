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
    private int id;
    private int stack;
    private Hand pocket;
    private Hand finalHand;
    private boolean isFolded;
    private boolean isBusted = false;
    private boolean isBigBlind = false;
    private boolean completedTurn = false;
    private boolean mustReveal;

    public Player(String name, int id, int startAmount) {
        this.name = name;
        this.id = id;
        stack = startAmount;
        pocket = new Hand();
        mustReveal = true;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void receiveCard(Card card) {
        pocket.addCard(card);
    }

    public boolean isFolded() {
        return isFolded;
    }

    public void setActive() {
        isFolded = false;
    }

    public boolean isBusted() {
        return isBusted;
    }

    public void bust() {
        isBusted = true;
    }

    public boolean isBigBlind() {
        return isBigBlind;
    }

    public void setBigBlind(boolean bigBlindStatus) {
        isBigBlind = bigBlindStatus;
    }

    public boolean completedTurn() {
        return completedTurn;
    }

    public void setCompletedTurn(boolean bool) {
        completedTurn = bool;
    }

    public int getStackValue() {
        return stack;
    }

    public void removeFromStack(int value) {
        stack -= value;
    }

    public void addToStack(int value) {
        stack += value;
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
        isFolded = true;
        mustReveal = false;
    }

    public boolean mustReveal() {
        return mustReveal;
    }
}