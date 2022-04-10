package hannahschroeder.texasholdem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Game {
    Table table;
    int startCash;
    List<Player> players;
    Scanner in;

    public Game(Scanner scanner, int startCash) {
        this.startCash = startCash;
        players = new ArrayList<>();
        table = new Table(players);
        in = scanner;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String[] Players = getPlayers(in);
        int startCash = getStartCash(in);

        Game game = new Game(in, startCash);
        for (String playerName : Players) {
            game.addPlayer(playerName);
        }

        game.play();

        in.close();
    }

    public void addPlayer(String playerName) {
        players.add(new Player(playerName, startCash));
    }

    public void play() {
        // TODO: move button

        // TODO: Play more than one round
        
        // determine big and small blind (amounts and players)
        playRound();
    }

    private static String[] getPlayers(Scanner in){
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

        String[] PlayerNames = new String[playerCount];
        for (int i = 0; i < playerCount; i++) {
            boolean nameIsUnique;
            do {
                System.out.println("Enter player name:");
                String playerName = in.nextLine();
                PlayerNames[i] = playerName;
                nameIsUnique = true;
                for (int j = i; j > 0; j--) {
                    if (playerName.equals(PlayerNames[j-1])) {
                        nameIsUnique = false;
                        System.out.println("That name is already in use.");
                    }
                }               
            } while (!nameIsUnique);
        }
        return PlayerNames;
    }

    private static int getStartCash(Scanner in) {
        boolean isValidAmount;
        int startCash = 0;
        do {
            System.out.println("How many chips should each player strart with? (minimum 3000)");
            String startCashString = in.nextLine();
            try {
                startCash = Integer.parseInt(startCashString);
                if (startCash < 3000) {
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

        // set all players not busted to active
        table.resetTable();

        // TODO: collect blinds

        // check retsult of playStage to decide whether to
        // continue playing
        while (table.playStage(in, roundWinners));

        if (roundWinners.size() == 0) {
            // determine players final hands
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                if (player.isActive()) {
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
            if (player.isActive()) {
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