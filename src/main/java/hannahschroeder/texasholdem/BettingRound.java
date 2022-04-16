package hannahschroeder.texasholdem;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class BettingRound {
    private Scanner in;
    private Table table;
    private List<Player> allPlayers;
    private List<Player> activePlayers;
    private Player dealer;
    private int currentBet = 0;
    private Map<Player, Integer> playerBets = new HashMap<>();
    
    public BettingRound(Table table, Scanner in) {
        this.in = in;
        this.table = table;
        allPlayers = table.getAllPlayers();
        activePlayers = table.getActivePlayers();
        dealer = table.getDealer();

        for (Player player : activePlayers) {
            playerBets.put(player, 0);
            player.setCompletedTurn(false);
        }
    }

    /**
     * plays a round of betting
     * @param isPreFlop true if the betting round is the pre-flop
     * @param roundWinners list of round winners
     * @return true if betting round resulted in a default win
     */
    public boolean play(boolean isPreFlop, List<Player> roundWinners) {
        setTurnsIncomplete();
        Player currentPlayer = table.getNextActivePlayer(dealer);
        int smallBlind = table.getSmallBlind();
        int bigBlind = 2*smallBlind;

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

            currentPlayer = table.getNextActivePlayer(currentPlayer);

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
            
            currentPlayer = table.getNextActivePlayer(currentPlayer);
        }

        do {
            int minimumBet = bigBlind;
            if (currentPlayer.getStackValue() < minimumBet) {
                minimumBet = currentPlayer.getStackValue();
            }

            /**
             * add logic for situation where all but one player is all in
             * so that even the one player who is in passes;
             * should be second condition to check for
             * create separate method boolean lastPlayerStanding(Player player)
             */
            PlayerAction action;
            EnumSet<PlayerAction> validActions;
            if (currentPlayer.getStackValue() == 0) {
                // player is already all in
                validActions = null;
            } else if (currentPlayer.getStackValue() + playerBets.get(currentPlayer) < currentBet) {
                // player cannot match the current bet
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.FOLD);
                // TODO: handle side pots
            } else if (currentPlayer.getStackValue() + playerBets.get(currentPlayer) == currentBet) {
                // player can match the current bet but must go all in to do so
                validActions = EnumSet.of(PlayerAction.CALL, PlayerAction.FOLD);
            } else if (minimumBet < bigBlind && playerBets.get(currentPlayer) == currentBet) {
                // betting would cause the player to go all in
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.CHECK, PlayerAction.FOLD);
            } else if (minimumBet < bigBlind) {
                // raising would cause the player to go all in
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.CALL, PlayerAction.FOLD);
            } else if (currentBet > 0 && playerBets.get(currentPlayer) == currentBet) {
                // player has placed a bet already as the big blind, and there are no other bets
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.RAISE, PlayerAction.CHECK, PlayerAction.FOLD);
            } else if (currentBet == 0) {
                // no betting has yet occurred and player can bet the big blind amount without going all in
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.BET, PlayerAction.CHECK, PlayerAction.FOLD);
            } else {
                // a bet has already been placed and player can raise by the big blind amount without going all in
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.RAISE, PlayerAction.CALL, PlayerAction.FOLD);
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
                    if (currentPlayer.getStackValue() + playerBets.get(currentPlayer) > currentBet) {
                        placeBet(currentPlayer, currentPlayer.getStackValue() + playerBets.get(currentPlayer));
                        setTurnsIncomplete();
                    } else if (currentPlayer.getStackValue() + playerBets.get(currentPlayer) < currentBet) {
                        // TODO: create side pots
                        placeBet(currentPlayer, currentPlayer.getStackValue() + playerBets.get(currentPlayer));
                        setTurnsIncomplete();
                    }
                    System.out.printf("%s goes all in%n", currentPlayer.getName());
                    break;
                }
                case FOLD: {
                    System.out.printf("%s folds%n", currentPlayer.getName());
                    currentPlayer.fold();
                }
            }

            currentPlayer.setCompletedTurn(true);

            if (currentPlayer.isBigBlind()) {
                currentPlayer.setBigBlind(false);
            }

            Player nextPlayer = table.getNextActivePlayer(currentPlayer);
            if (action == PlayerAction.FOLD) {
                activePlayers.remove(currentPlayer);
                currentPlayer = nextPlayer;
                if (checkDefaultWin(roundWinners)) {
                    return true;
                }
            } else {
                currentPlayer = nextPlayer;
            }
        } while (!currentPlayer.completedTurn());

        // TODO: handle side pot scenario
        moveBetsToCenterPot();

        return false;
    }

    private int getBetAmount(Player player, int min) {
        int max = player.getStackValue() + playerBets.get(player);
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
            if(!isValidAmount) {
                System.out.println("Invalid input");
            }

        } while (!isValidAmount);

        return amount;
    }

    /**
     * sets the current bet for the table and transfers the player's
     * chips to their local betting pot
     * @param player player who is betting/calling
     * @param betAmount total amount of the bet
     */
    private void placeBet(Player player, int betAmount) {
        currentBet = betAmount;

        int transferAmount = betAmount - playerBets.get(player);
        player.removeFromStack(transferAmount);
        playerBets.put(player, betAmount);
    }

    private int getBetsTotal() {
        int betsTotal = 0;
        for (Map.Entry<Player, Integer> entry : playerBets.entrySet()) {
            betsTotal += entry.getValue();
        }
        return betsTotal;
    }

    private void moveBetsToCenterPot() {
        int bets = getBetsTotal();
        int potValue = table.getCenterPot().getTotal();
        table.setCenterPot(potValue + bets);
    }

    /**
     * 
     * @param roundWinners a list to fill with the winning players
     * @return true if there was a winner, false otherwise
     */
    private boolean checkDefaultWin(List<Player> roundWinners) {
        Player winner = null;

        if (activePlayers.size() == 1) {
            winner = activePlayers.get(0);
            winner.dontReveal();
            roundWinners.add(winner);

            //put player bets into table's center pot
            moveBetsToCenterPot();
            return true;
        }

        return false;
    }

    private void setTurnsIncomplete() {
        for (Player player : allPlayers) {
            if (!player.isFolded() && !player.isBusted() && player.getStackValue() > 0) {
                player.setCompletedTurn(false);
            }
        }
    }
}