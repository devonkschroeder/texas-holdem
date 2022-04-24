package hannahschroeder.texasholdem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Table {
    private Playerlist players;
    private int startCash;
    private List<Pot> pots = new ArrayList<>();
    private int smallBlind = 25;
    private int dealerToken = 0;
    private Hand communityCards;
    private Stage currentStage;
    private Deck deck;

    public Table(Playerlist players, int startCash) {
        this.players = players;
        this.startCash = startCash;
        pots.add(new Pot(0));
        currentStage = Stage.PREFLOP;
        deck = new Deck(true);
        communityCards = new Hand();
    }

    public void resetTable() {
        pots = new ArrayList<>();
        pots.add(new Pot(0));
        for (Player player : players.getPlayers()) {            
            if (!player.isBusted()) {
                player.setActive();
                player.resetHand();
                player.setBigBlind(false);
                pots.get(0).addPotentialWinner(player);
            }
        }
        currentStage = Stage.PREFLOP;
        deck = new Deck(true);
        communityCards = new Hand();
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public List<Pot> getPots() {
        return pots;
    }

    public Player getDealer() {
        return players.getPlayerById(dealerToken);
    }

    public void moveDealerToken() {
        if (dealerToken == players.getPlayerCount() - 1) {
            dealerToken = 0;
            increaseBlinds();
        } else {
            dealerToken++;
        }
        if (getDealer().isBusted()) {
            moveDealerToken();
        }
    }

    public void increaseBlinds() {
        int newSmallBlind;

        switch (smallBlind) {
            case 25:
                newSmallBlind = 50;
                break;
            case 50:
                newSmallBlind = 75;
                break;
            case 75:
                newSmallBlind = 100;
                break;
            case 100:
                newSmallBlind = 200;
                break;
            case 200:
                newSmallBlind = 300;
                break;
            case 300:
                newSmallBlind = 500;
                break;
            case 500:
                newSmallBlind = 1000;
                break;
            case 1000:
                newSmallBlind = 1500;
                break;
            case 1500:
                newSmallBlind = 2000;
                break;
            case 2000:
                newSmallBlind = 4000;
                break;
            case 4000:
                newSmallBlind = 8000;
                break;
            case 8000:
                newSmallBlind = 15000;
                break;
            case 15000:
                newSmallBlind = 25000;
                break;
            case 25000:
                newSmallBlind = 40000;
                break;
            default:
                // maxiumum small blind amount reached, do nothing
                newSmallBlind = smallBlind;
                break;
        }

        if (2*newSmallBlind > startCash) {
            // big blind should never exceed the start cash amount
            return;
        }

        smallBlind = newSmallBlind;
    }

    /**
     * plays through the current stage
     * @param in
     * @param roundWinners
     * @return true when the game should continue onto the next stage,
     * false when someone has won the hand and play stops
     */
    public boolean playStage(Scanner in, List<Player> roundWinners) {
        System.out.println();
        
        switch (currentStage) {
            case PREFLOP:
                // deal to active players
                dealHand();
                System.out.println("Pre-flop:");
                break;
            case FLOP:
                // flop
                deck.burn();
                communityCards.addCard(deck.drawCard());
                communityCards.addCard(deck.drawCard());
                communityCards.addCard(deck.drawCard());

                System.out.print("Flop: ");
                System.out.println(showCards());
                break;
            case TURN:
                // turn
                deck.burn();
                communityCards.addCard(deck.drawCard());
                System.out.print("Turn: ");
                System.out.println(showCards());
                break;
            case RIVER:
                // river
                deck.burn();
                communityCards.addCard(deck.drawCard());
                System.out.print("River: ");
                System.out.println(showCards());
                break;
            default:
                throw new RuntimeException("Unexpected stage");
        }

        for (int i = 0; i < pots.size(); i++) {
            if (i == 0) {
                System.out.printf("Pot: %d%n", pots.get(i).getTotal());
            } else {
                System.out.printf("Side pot: %d%n", pots.get(i).getTotal());
            }
        }

        for (Player player : players.getActivePlayers()) {
            System.out.printf("%s: %d%n", player.getName(), player.getStackValue());
        }

        BettingRound bettingRound = new BettingRound(pots, players, getDealer(), in);
        if (currentStage == Stage.PREFLOP) {
            if (bettingRound.play(getSmallBlind(), true, roundWinners)) {
                return false;
            }
        } else {
            if (bettingRound.play(getSmallBlind(), false, roundWinners)) {
                return false;
            }
        }

        if (currentStage != Stage.RIVER) {
            currentStage = currentStage.nextStage();
            return true;
        } else {
            return false;
        }
    }

    public String showCards() {
        return communityCards.toString();
    }

    public Hand getHand() {
        return communityCards;
    }

    public void revealHands() {
        for (Player player : players.getPlayers()) {
            if (!player.isFolded() && !player.isBusted()) {
                String message = "";
                if (player.mustReveal()) {
                    message += String.format("%s has: %s", player.getName(), player.getPrivateHand().toString());
                    if (player.getFinalHand() != null) {
                        message += String.format(" (%s) %s", player.getFinalHand(), HandRank.handRankString(player.getFinalHand()));
                    }

                    System.out.println(message);
                }
            }            
        }
    }

    private void dealHand() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < players.getPlayers().size(); j++) {
                Player player = players.getPlayers().get(j);
                if (!player.isFolded() && !player.isBusted()) {
                    player.receiveCard(deck.drawCard());
                }                
            }
        }
    }
}