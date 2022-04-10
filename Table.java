import java.util.List;
import java.util.Scanner;

class Table {
    List<Player> players;
    /** Number of players who are still in the round */
    int activePlayers;
    Hand communityCards;
    Stage currentStage;
    Deck deck;
    Pot centerPot;

    public Table(List<Player> players) {
        this.players = players;
        resetTable();
    }

    public void resetTable() {
        communityCards = new Hand();
        activePlayers = 0;
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.stackValue() > 0) {
                player.setActive();
                addActivePlayer();
            }
        }
        currentStage = Stage.PREFLOP;
        deck = new Deck(true);
    }

    /**
     * 
     * @param in
     * @param roundWinners
     * @return true if the hand isn't over, false otherwise
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

                System.out.println("Flop:");
                System.out.println(showCards());
                break;
            case TURN:
                // turn
                deck.burn();
                communityCards.addCard(deck.drawCard());
                System.out.println("Turn:");
                System.out.println(showCards());
                break;
            case RIVER:
                // river
                deck.burn();
                communityCards.addCard(deck.drawCard());
                System.out.println("River:");
                System.out.println(showCards());
                break;
            default:
                throw new RuntimeException("Unexpected stage");
        }

        // reveal hands (testing purposes only)
        revealHands();

        // betting round
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.isActive()) {
                playerAction(player, in);
                if (checkWin(roundWinners)) {
                    return false;
                }
            }
        }

        if (currentStage != Stage.RIVER) {
            currentStage = currentStage.nextStage();
            return true;
        } else {
            return false;
        }
    }

    public void addActivePlayer() {
        activePlayers++;
    }

    public void removeActivePlayer() {
        activePlayers--;
    }

    public String showCards() {
        return communityCards.toString();
    }

    public Hand getHand() {
        return communityCards;
    }

    public void revealHands() {
        for (Player player : players) {
            if (player.isActive()) {
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
                if (player.isActive()) {
                    player.receiveCard(deck.drawCard());
                }                
            }
        }
    }

    private void playerAction(Player player, Scanner in) {
        // get input from player
        PlayerAction action = PlayerAction.getActionFromScanner(in, player);
        
        // execute player action
        switch (action) {
            case BET_OR_RAISE:
                // TODO: Handle raise/bet
                break;
            case CALL_OR_CHECK:
                // TODO: Handle call/check
                break;
            case FOLD:
                player.fold();
                removeActivePlayer();
                break;
        }
    }

    /**
     * 
     * @param roundWinners a list to fill with the winning players
     * @return true if there was a winner, false otherwise
     */
    private boolean checkWin(List<Player> roundWinners) {
        // check for win condition
        Player winner = null;

        if (activePlayers == 1) {
            for (Player player : players) {
                if (player.isActive()) {
                    winner = player;
                }
            }
            winner.dontReveal();
            roundWinners.add(winner);
            return true;
        }

        return false;
    }
}