package hannahschroeder.texasholdem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Game {
    private Table table;
    private int startCash;
    private List<Player> players;
    private Scanner in;

    public Game(Scanner scanner, int startCash) {
        this.startCash = startCash;
        players = new ArrayList<>();
        table = new Table(players, startCash, this);
        in = scanner;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String[] playerNames = getPlayerNames(in);
        int startCash = getStartCash(in);

        Game game = new Game(in, startCash);
        for (int i = 0; i < playerNames.length; i++) {
            game.addPlayer(playerNames[i], i);
        }

        game.play();

        in.close();
    }

    public void addPlayer(String playerName, int id) {
        players.add(new Player(playerName, id, startCash));
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
        for (Player player : players) {
            if (!player.isBusted()) {
                gameWinner = player;
            }
        }
        
        System.out.printf("%s has won the game.%n", gameWinner.getName());
    }

    public Player getNextPlayer(Player previousPlayer) {
        Player nextPlayer = null;
        if (previousPlayer.getId() == players.size() - 1) {
            nextPlayer = players.get(0);
        } else {
            nextPlayer = players.get(previousPlayer.getId() + 1);
        }
        return nextPlayer;
    }

    private boolean gameOver() {
        int unbustedPlayers = 0;
        for (Player player : players) {
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
            System.out.println("How many chips should each player strart with? (min 3000, max 1000000)");
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

        if (roundWinners.size() == 0) {
            // determine players final hands
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                if (!player.isFolded()) {
                    player.determineFinalHand(table.getHand());
                }
            }

            // reveal players final hands
            table.revealHands();

            // determine winners
            determineRoundWinners(roundWinners);
        }

        // distribute winnings and announce winners
        distributePots(roundWinners, table.getPots());
        announceRoundWinners(roundWinners);
        transferWinnings(roundWinners);

        // bust players with empty stacks
        for (Player player : players) {
            if (player.getStackValue() == 0) {
                player.bust();
            }
        }
    }

    private void determineRoundWinners(List<Player> winners) {
        for (Pot pot : table.getPots()) {
            determinePotWinners(winners, pot);
        }
    }

    private void determinePotWinners(List<Player> winners, Pot pot) {
        List<Player> potentialWinners = pot.getPotentialWinners();
        List<Player> potWinners = new ArrayList<>();
        for (Player player : potentialWinners) {
            if (potWinners.size() == 0) {
                potWinners.add(player);
            } else if (potWinners.get(0).getFinalHand().compareTo(player.getFinalHand()) == 0) {
                potWinners.add(player);
            } else if (potWinners.get(0).getFinalHand().compareTo(player.getFinalHand()) < 0) {
                potWinners.clear();
                potWinners.add(player);
            }
        }

        for (Player player : potWinners) {
            if (!winners.contains(player)) {
                winners.add(player);
            }
        }
    }

    private void distributePots(List<Player> winners, List<Pot> pots) {
        for (Pot pot : pots) {
            pot.distribute(winners);
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