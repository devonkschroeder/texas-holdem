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
        Player currentPlayer = table.getNextActivePlayer(dealer);
        int smallBlind = table.getSmallBlind();
        int bigBlind = 2*smallBlind;

        if (isPreFlop) {
            System.out.printf("%s is dealer.%n", dealer.getName());

            // posting of small blind
            increaseBet(currentPlayer, smallBlind, 0);
            System.out.printf("%s posts small blind of %d.%n", currentPlayer.getName(), currentBet);

            currentPlayer = table.getNextActivePlayer(currentPlayer);

            // posting of big blind
            currentPlayer.setBigBlind(true);
            increaseBet(currentPlayer, bigBlind, 0);
            System.out.printf("%s posts big blind of %d.%n", currentPlayer.getName(), currentBet);

            currentPlayer = table.getNextActivePlayer(currentPlayer);
        }

        /**
         * TODO: handle side pots/inability to match bet
         */
        do {
            PlayerAction action;
            EnumSet<PlayerAction> validActions;
            if (currentPlayer.getStackValue() + playerBets.get(currentPlayer) <= currentBet) {
                validActions = EnumSet.of(PlayerAction.ALL_IN, PlayerAction.FOLD);
            } else if (currentPlayer.isBigBlind() && currentBet == bigBlind) {
                validActions = EnumSet.of(PlayerAction.CHECK, PlayerAction.RAISE, PlayerAction.ALL_IN, PlayerAction.FOLD);
            } else if (currentBet > 0) {
                validActions = EnumSet.of(PlayerAction.CALL, PlayerAction.RAISE, PlayerAction.ALL_IN, PlayerAction.FOLD);
            } else {
                validActions = EnumSet.of(PlayerAction.CHECK, PlayerAction.BET, PlayerAction.ALL_IN, PlayerAction.FOLD);
            }
            action = PlayerAction.getActionFromScanner(in, currentPlayer, validActions, getBetsTotal(), currentBet);

            int existingBet = playerBets.get(currentPlayer);
            int allInAmount = currentPlayer.getStackValue() + existingBet;

            switch (action) {
                case BET: { // refactor to utilize increaseBet() method
                    int betAmount = getAmount(currentPlayer, bigBlind, currentPlayer.getStackValue());
                    currentBet = betAmount;
                    currentPlayer.removeFromStack(betAmount);
                    playerBets.put(currentPlayer, betAmount);
                    System.out.printf("%s bets %d%n", currentPlayer.getName(), betAmount);
                    setTurnsIncomplete();
                    break;
                }
                case RAISE: { // refactor to utilize increaseBet() method
                    int minRaise = currentBet + bigBlind;
                    int raiseAmount = getAmount(currentPlayer, minRaise, allInAmount);
                    currentBet = raiseAmount;
                    currentPlayer.removeFromStack(raiseAmount - existingBet);
                    playerBets.put(currentPlayer, raiseAmount);
                    System.out.printf("%s raises to %d%n", currentPlayer.getName(), raiseAmount);
                    setTurnsIncomplete();
                    break;
                }
                case CALL: {
                    currentPlayer.removeFromStack(currentBet - playerBets.get(currentPlayer));
                    playerBets.put(currentPlayer, currentBet);
                    System.out.printf("%s calls%n", currentPlayer.getName());
                    break;
                }
                case CHECK: {
                    System.out.printf("%s checks%n", currentPlayer.getName());
                    break;
                }
                case ALL_IN: {
                    if (allInAmount > currentBet) { // refactor to utilize increaseBet() method
                        currentBet = allInAmount;
                        setTurnsIncomplete();
                    } else if (allInAmount < currentBet) {
                        // TODO: create side pots
                    }
                    currentPlayer.removeFromStack(allInAmount - existingBet);
                    playerBets.put(currentPlayer, allInAmount);
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

    private int getAmount(Player player, int min, int max) {
        boolean isValidAmount;
        int amount = 0;
        do {
            System.out.printf("How many chips would you like to bet/raise? (min %d, max %d)%n", min, max);
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
     * resets the going bet for the table, tracks who made the last bet/raise,
     * and transfers the player's chips to their local betting pot
     * @param player player who is raising/starting the betting
     * @param betAmount total amount of the bet
     * @param previousBet amount player had previously contributed towards the bet
     */
    private void increaseBet(Player player, int betAmount, int previousBet) {
        currentBet = betAmount;

        int transferAmount = betAmount - previousBet;
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
        int pot = table.getCenterPot();
        table.setCenterPot(pot + bets);
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