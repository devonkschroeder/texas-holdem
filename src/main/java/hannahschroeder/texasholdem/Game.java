package hannahschroeder.texasholdem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Game {
    private Table table;
    private Playerlist players;
    private Scanner in;

    public Game(Scanner scanner, int startCash, String[] playerNames) {
        players = new Playerlist();
        for (int i = 0; i < playerNames.length; i++) {
            players.addPlayer(playerNames[i], startCash);
        }
        table = new Table(players, startCash);
        in = scanner;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String[] playerNames = getPlayerNames(in);
        int startCash = getStartCash(in);

        Game game = new Game(in, startCash, playerNames);

        game.play();

        in.close();
    }

    public void play() {
        while (!gameOver()) {
            playRound();
            if (!gameOver()) {
                table.moveDealerToken();
            }
            System.out.println("");
        }

        Player gameWinner = null;
        for (Player player : players.getPlayers()) {
            if (!player.isBusted()) {
                gameWinner = player;
            }
        }
        
        System.out.printf("%s has won the game.%n", gameWinner.getName());
    }

    private boolean gameOver() {
        int unbustedPlayers = 0;
        for (Player player : players.getPlayers()) {
            if (!player.isBusted()) {
                unbustedPlayers++;
            }
            if (unbustedPlayers > 1) {
                return false;
            }
        }
        return true;
    }

    private static String[] getPlayerNames(Scanner in) {
        boolean isValidPlayerCount;
        int playerCount = 0;
        do {
            System.out.println("How many players will be playing? (2-12)");
            String playerCountString = in.nextLine();
            try {
                playerCount = Integer.parseInt(playerCountString);
                if (playerCount < 2 || playerCount > 12) {
                    isValidPlayerCount = false;
                } else {
                    isValidPlayerCount = true;
                }
            } catch (Exception e) {
                isValidPlayerCount = false;
            }
            if (!isValidPlayerCount) {
                System.out.println("Invalid input");
            }

        } while (!isValidPlayerCount);

        String[] playerNames = new String[playerCount];
        for (int i = 0; i < playerCount; i++) {
            boolean nameIsUnique;
            do {
                System.out.println("Enter player name:");
                String playerName = in.nextLine();
                playerNames[i] = playerName;
                nameIsUnique = true;
                for (int j = i; j > 0; j--) {
                    if (playerName.equals(playerNames[j - 1])) {
                        nameIsUnique = false;
                        System.out.println("Invalid input - name must be unique");
                    }
                }
            } while (!nameIsUnique);
        }
        return playerNames;
    }

    private static int getStartCash(Scanner in) {
        boolean isValidAmount;
        int startCash = 0;
        do {
            System.out.println("How many chips should each player start with? (min 3000, max 1000000)");
            String startCashString = in.nextLine();
            try {
                startCash = Integer.parseInt(startCashString);
                if (startCash < 3000 || startCash > 1000000) {
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
        return startCash;
    }

    private void playRound() {
        List<Player> roundWinners = new ArrayList<>();

        table.resetTable();

        while (table.playStage(in, roundWinners));

        List<Player> playersList = players.getPlayers();
        if (players.getActivePlayers().size() != 1) { // no default winner
            // determine players final hands
            for (int i = 0; i < playersList.size(); i++) {
                Player player = playersList.get(i);
                if (!player.isFolded()) {
                    player.determineFinalHand(table.getHand());
                }
            }

            // reveal players final hands
            table.revealHands();
        }

        // determine winners
        determineRoundWinners(roundWinners);

        // distribute winnings and announce winners
        distributePots(table.getPots());
        announceRoundWinners(roundWinners);
        transferWinnings(roundWinners);

        // bust players with empty stacks
        for (Player player : playersList) {
            if (player.getStackValue() == 0) {
                player.bust();
            }
        }
    }

    private void determineRoundWinners(List<Player> winners) {
        for (Pot pot : table.getPots()) {
            determinePotWinners(pot);

            for (Player player : pot.getWinners()) {
                if (!winners.contains(player)) {
                    winners.add(player);
                }
            }
        }
    }

    private void determinePotWinners(Pot pot) {
        List<Player> potentialWinners = pot.getPotentialWinners();
        // Set pot winners
        for (Player player : potentialWinners) {
            if (pot.getWinners().size() == 0) {
                pot.addWinner(player);
            } else if (pot.getWinners().get(0).getFinalHand().compareTo(player.getFinalHand()) == 0) {
                pot.addWinner(player);
            } else if (pot.getWinners().get(0).getFinalHand().compareTo(player.getFinalHand()) < 0) {
                pot.clearWinners();
                pot.addWinner(player);
            }
        }
    }

    private void distributePots(List<Pot> pots) {
        for (Pot pot : pots) {
            pot.distribute(table.getDealer(), players);
        }
    }

    private void announceRoundWinners(List<Player> roundWinners) {
        for (Player winner : roundWinners) {
            System.out.printf("%s wins %d%n", winner.getName(), winner.getWinnings());
        }
    }

    private void transferWinnings(List<Player> roundWinners) {
        for (Player player : roundWinners) {
            player.addToStack(player.getWinnings());
            player.setWinnings(0);
        }
    }
}