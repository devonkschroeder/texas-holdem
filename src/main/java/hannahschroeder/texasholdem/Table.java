package hannahschroeder.texasholdem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Table {
    private Game game;
    private List<Player> players;
    private int startCash;
    private int centerPot = 0;
    private int smallBlind = 25;
    private int dealerToken = 0;
    private Hand communityCards;
    private Stage currentStage;
    private Deck deck;

    public Table(List<Player> players, int startCash, Game game) {
        this.game = game;
        this.players = players;
        this.startCash = startCash;
        resetTable();
    }

    public void resetTable() {
        communityCards = new Hand();
        for (Player player : players) {
            if (player.getStackValue() == 0) {
                player.bust();
            } else {
                player.setActive();
            }
        }
        currentStage = Stage.PREFLOP;
        deck = new Deck(true);
    }

    public List<Player> getAllPlayers() {
        return players;
    }

    public Player getNextPlayer(Player previousPlayer) {
        return game.getNextPlayer(previousPlayer);
    }

    public Player getNextActivePlayer(Player previousPlayer) {
        Player nextPlayer;

        do {
            nextPlayer = getNextPlayer(previousPlayer);
            previousPlayer = nextPlayer;
        } while (nextPlayer.isFolded() || nextPlayer.isBusted());

        return nextPlayer;
    }

    public List<Player> getActivePlayers() {
        List<Player> activePlayers = new ArrayList<>();
        for (Player player : players) {
            if (!player.isFolded() && !player.isBusted()) {
                activePlayers.add(player);
            }
        }
        return activePlayers;
    }

    public Player getPlayerById(int id) {
        Player player = null;
        for (Player p : players) {
            if (p.getId() == id) {
                player = p;
            }
        }

        return player;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getCenterPot() {
        return centerPot;
    }

    public void setCenterPot(int amount) {
        centerPot = amount;
    }

    public Player getDealer() {
        return players.get(dealerToken);
    }

    public void moveDealerToken() {
        if (dealerToken == players.size() - 1) {
            dealerToken = 0;
            increaseBlinds();
        } else {
            dealerToken++;
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
        switch (currentStage) {
            case PREFLOP:
                // deal to active players
                dealHand();
                break;
            case FLOP:
                // flop
                deck.burn();
                communityCards.addCard(deck.drawCard());
                communityCards.addCard(deck.drawCard());
                communityCards.addCard(deck.drawCard());

                System.out.print("Flop: ");
                System.out.print(showCards());
                break;
            case TURN:
                // turn
                deck.burn();
                communityCards.addCard(deck.drawCard());
                System.out.print("Turn: ");
                System.out.print(showCards());
                break;
            case RIVER:
                // river
                deck.burn();
                communityCards.addCard(deck.drawCard());
                System.out.print("River: ");
                System.out.print(showCards());
                break;
            default:
                throw new RuntimeException("Unexpected stage");
        }

        System.out.printf(" (pot: %d)%n", getCenterPot());

        BettingRound bettingRound = new BettingRound(this, in);
        if (currentStage == Stage.PREFLOP) {
            if (bettingRound.play(true, roundWinners)) {
                return false;
            }
        } else {
            if (bettingRound.play(false, roundWinners)) {
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
        for (Player player : players) {
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
            for (int j = 0; j < players.size(); j++) {
                Player player = players.get(j);
                if (!player.isFolded() && !player.isBusted()) {
                    player.receiveCard(deck.drawCard());
                }                
            }
        }
    }
}