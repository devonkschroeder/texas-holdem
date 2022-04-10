import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Game {
    Table table;
    int startAmount;
    List<Player> players;
    Scanner in;

    public Game(Scanner scanner, int startAmount) {
        this.startAmount = startAmount;
        players = new ArrayList<>();
        table = new Table(players);
        in = scanner;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        Game game = new Game(in, 2500);
        game.addPlayer("Player1");
        game.addPlayer("Player2");

        game.play();

        in.close();
    }

    public void addPlayer(String playerName) {
        players.add(new Player(playerName, startAmount));
    }

    public void play() {
        // TODO: move button

        // TODO: Play more than one round
        
        // determine big and small blind (amounts and players)
        playRound();
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