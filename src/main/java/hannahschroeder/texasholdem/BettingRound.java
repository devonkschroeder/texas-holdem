package hannahschroeder.texasholdem;

import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;

class BettingRound {
    private Scanner in;
    private List<Pot> pots;
    private Playerlist allPlayers;
    private List<Player> activePlayers;
    private Player dealer;
    private int currentBet = 0;

    public BettingRound(List<Pot> pots, Playerlist players, Player dealer, Scanner in) {
        this.pots = pots;
        this.in = in;
        allPlayers = players;
        activePlayers = players.getActivePlayers();
        this.dealer = dealer;

        for (Player player : activePlayers) {
            player.setBet(0);
            player.setCompletedTurn(false);
        }
    }

    /**
     * plays a round of betting
     * 
     * @param isPreFlop    true if the betting round is the pre-flop
     * @param roundWinners list of round winners
     * @return true if betting round resulted in a default win
     */
    public boolean play(int smallBlind, boolean isPreFlop, List<Player> roundWinners) {
        setTurnsIncomplete();
        Player currentPlayer = allPlayers.getNextActivePlayer(dealer);
        int bigBlind = 2 * smallBlind;

        if (isPreFlop) {
            System.out.printf("%s is dealer.%n", dealer.getName());

            // posting of small blind
            if (currentPlayer.getStackValue() < smallBlind) {
                placeBet(currentPlayer, currentPlayer.getStackValue());
                System.out.printf("%s (small blind) goes all in.%n", currentPlayer.getName());
            } else {
                placeBet(currentPlayer, smallBlind);
                System.out.printf("%s posts small blind of %d.%n", currentPlayer.getName(), currentBet);
            }

            currentPlayer = allPlayers.getNextActivePlayer(currentPlayer);

            // posting of big blind
            currentPlayer.setBigBlind(true);
            if (currentPlayer.getStackValue() < bigBlind) {
                placeBet(currentPlayer, currentPlayer.getStackValue());
                System.out.printf("%s (big blind) goes all in.%n", currentPlayer.getName());
                System.out.printf("Big blind is %d.%n", bigBlind);
                currentBet = bigBlind;
            } else {
                placeBet(currentPlayer, bigBlind);
                System.out.printf("%s posts big blind of %d.%n", currentPlayer.getName(), currentBet);
            }

            currentPlayer = allPlayers.getNextActivePlayer(currentPlayer);
        }

        do {
            int minimumBet = bigBlind;
            if (currentPlayer.getStackValue() < minimumBet) {
                minimumBet = currentPlayer.getStackValue();
            }

            PlayerAction action;
            EnumSet<PlayerAction> validActions;
            if (currentPlayer.getStackValue() == 0) {
                // player is already all in
                validActions = null;
            } else if (lastPlayerStanding(currentPlayer)) {
                // player is the only active player that can bet
                validActions = null;
            } else if (currentPlayer.getStackValue() + currentPlayer.getBet() < currentBet) {
                // player cannot match the current bet
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.FOLD);
            } else if (currentPlayer.getStackValue() + currentPlayer.getBet() == currentBet) {
                // player can match the current bet but must go all in to do so
                validActions = EnumSet.of(PlayerAction.CALL, PlayerAction.FOLD);
            } else if (minimumBet < bigBlind && currentPlayer.getBet() == currentBet) {
                // betting would cause the player to go all in
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.CHECK, PlayerAction.FOLD);
            } else if (minimumBet < bigBlind) {
                // raising would cause the player to go all in
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.CALL, PlayerAction.FOLD);
            } else if (currentBet > 0 && currentPlayer.getBet() == currentBet) {
                // player has placed a bet already as the big blind, and there are no other bets
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.RAISE, PlayerAction.CHECK,
                        PlayerAction.FOLD);
            } else if (currentBet == 0) {
                // no betting has yet occurred and player can bet the big blind amount without
                // going all in
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.BET, PlayerAction.CHECK, PlayerAction.FOLD);
            } else {
                // a bet has already been placed and player can raise by the big blind amount
                // without going all in
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.RAISE, PlayerAction.CALL,
                        PlayerAction.FOLD);
            }
            action = PlayerAction.getActionFromScanner(in, currentPlayer, validActions, getBetsTotal(), currentBet);

            switch (action) {
                case PASS: {
                    break;
                }
                case BET: {
                    int betAmount = getBetAmount(currentPlayer, minimumBet);
                    placeBet(currentPlayer, betAmount);
                    System.out.printf("%s bets %d%n", currentPlayer.getName(), betAmount);
                    setTurnsIncomplete();
                    break;
                }
                case RAISE: {
                    int minRaise = currentBet + minimumBet;
                    int raiseAmount = getBetAmount(currentPlayer, minRaise);
                    placeBet(currentPlayer, raiseAmount);
                    System.out.printf("%s raises to %d%n", currentPlayer.getName(), raiseAmount);
                    setTurnsIncomplete();
                    break;
                }
                case CALL: {
                    placeBet(currentPlayer, currentBet);
                    System.out.printf("%s calls%n", currentPlayer.getName());
                    break;
                }
                case CHECK: {
                    System.out.printf("%s checks%n", currentPlayer.getName());
                    break;
                }
                case ALL_IN: {
                    if (currentPlayer.getStackValue() + currentPlayer.getBet() > currentBet) {
                        placeBet(currentPlayer, currentPlayer.getStackValue() + currentPlayer.getBet());
                        setTurnsIncomplete();
                    } else if (currentPlayer.getStackValue() + currentPlayer.getBet() < currentBet) {
                        placeBet(currentPlayer, currentPlayer.getStackValue() + currentPlayer.getBet());
                        setTurnsIncomplete();
                    }
                    System.out.printf("%s goes all in%n", currentPlayer.getName());
                    break;
                }
                case FOLD: {
                    System.out.printf("%s folds%n", currentPlayer.getName());
                    for (Pot pot : pots) {
                        if (pot.getPotentialWinners().contains(currentPlayer)) {
                            pot.removePotentialWinner(currentPlayer);
                        }
                    }
                    currentPlayer.fold();
                }
            }

            currentPlayer.setCompletedTurn(true);

            if (currentPlayer.isBigBlind()) {
                currentPlayer.setBigBlind(false);
            }

            Player nextPlayer = allPlayers.getNextActivePlayer(currentPlayer);
            if (action == PlayerAction.FOLD) {
                currentPlayer = nextPlayer;
                if (checkDefaultWin()) {
                    moveBetsToPots();
                    return true;
                }
            } else {
                currentPlayer = nextPlayer;
            }
        } while (!currentPlayer.completedTurn());

        moveBetsToPots();
        return false;
    }

    private boolean lastPlayerStanding(Player currentPlayer) {
        for (Player player : allPlayers.getActivePlayers()) {
            if (player.getStackValue() > 0) {
                if (player != currentPlayer) {
                    return false;
                } else {
                    if (currentPlayer.getBet() < currentBet) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private int getBetAmount(Player player, int min) {
        int max = player.getStackValue() + player.getBet();
        boolean isValidAmount;
        int amount = 0;

        do {
            System.out.printf("How much would you like to bet/raise? (min %d, max %d)%n", min, max);
            String amountString = in.nextLine();
            try {
                amount = Integer.parseInt(amountString);
                if (amount < min || amount > max) {
                    isValidAmount = false;
                } else {
                    isValidAmount = true;
                }
            } catch (Exception e) {
                isValidAmount = false;
            }
            if (!isValidAmount) {
                System.out.println("Invalid input");
            }

        } while (!isValidAmount);

        return amount;
    }

    /**
     * sets the current bet for the table and transfers the player's
     * chips to their local betting pot
     * 
     * @param player    player who is betting/calling
     * @param betAmount total amount of the bet
     */
    private void placeBet(Player player, int betAmount) {
        currentBet = betAmount;

        int transferAmount = betAmount - player.getBet();
        player.removeFromStack(transferAmount);
        player.setBet(betAmount);
    }

    private int getBetsTotal() {
        int betsTotal = 0;
        for (Player player : activePlayers) {
            betsTotal += player.getBet();
        }
        return betsTotal;
    }

    private void moveBetsToPots() {
        int lowestBet = currentBet;
        while (getBetsTotal() > 0) {
            Pot currentPot = pots.get(pots.size() - 1);
            for (Player player : currentPot.getPotentialWinners()) {
                if (player.getBet() < lowestBet) {
                    lowestBet = player.getBet();
                }
            }

            // move lowestBet amount to current pot
            for (Player player : activePlayers) {
                int bet = player.getBet();
                if (player.getBet() < lowestBet) {
                    currentPot.addToPot(bet);
                    player.setBet(0);
                } else {
                    currentPot.addToPot(lowestBet);
                    player.setBet(bet - lowestBet);
                }
            }

            // create side pot
            Pot newPot = new Pot(0);
            if (getBetsTotal() > 0) {
                pots.add(newPot);
                // add potential winners of pot to pot
                for (Player player : currentPot.getPotentialWinners()) {
                    if (player.getBet() > 0) {
                        newPot.addPotentialWinner(player);
                    }
                }
            }

            /**
             * if the newest pot has the same potential winners as the
             * previous one, remove it.
             */
            if (currentPot.getPotentialWinners().containsAll(newPot.getPotentialWinners())
                    && newPot.getPotentialWinners().containsAll(currentPot.getPotentialWinners())) {
                // combine pots
                pots.remove(newPot);
            }
        }
    }
    
    private boolean checkDefaultWin() {
        List<Player> remainingPlayers = allPlayers.getActivePlayers();
        return remainingPlayers.size() == 1;
    }

    private void setTurnsIncomplete() {
        for (Player player : allPlayers.getPlayers()) {
            if (!player.isFolded() && !player.isBusted() && player.getStackValue() > 0) {
                player.setCompletedTurn(false);
            }
        }
    }
}