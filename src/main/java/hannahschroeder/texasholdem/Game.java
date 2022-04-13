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
        table = new Table(players, startCash);
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
        // TODO: Play more than one round
        
        playRound();

        table.moveDealerToken();
    }

    private static String[] getPlayerNames(Scanner in){
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
            if(!isValidPlayerCount) {
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
                    if (playerName.equals(playerNames[j-1])) {
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
            if(!isValidAmount) {
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
            determineWinners(roundWinners);
        }

        // announce winners
        announceWinners(roundWinners);

        // TODO: distribute winnings
    }

    private void determineWinners(List<Player> winners) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (!player.isFolded() && !player.isBusted()) {
                if (winners.size() == 0) {
                    winners.add(player);
                } else if (winners.get(0).getFinalHand().compareTo(player.getFinalHand()) == 0) {
                    winners.add(player);
                } else if (winners.get(0).getFinalHand().compareTo(player.getFinalHand()) < 0) {
                    winners.clear();
                    winners.add(player);
                }
            }
        }
    }

    // TODO: modify to include information about pot value won
    // TODO: distinguish between round winners and game winner
    private void announceWinners(List<Player> winners) {
        if (winners.size() == 1) {
            Player winner = winners.get(0);
            System.out.printf("Winner is %s%n", winner.getName());
        } else {
            System.out.print("Winners are: ");
            for (Player winner : winners) {
                System.out.printf("%s%n", winner.getName());
            }
        }
    }
}