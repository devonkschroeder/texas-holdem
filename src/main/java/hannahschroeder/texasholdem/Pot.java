package hannahschroeder.texasholdem;

import java.util.ArrayList;
import java.util.List;

class Pot {
    Table table;
    int potTotal;
    List<Player> potentialWinners = new ArrayList<>();

    public Pot(Table table, int startAmount) {
        this.table = table;
        potTotal = startAmount;
    }

    public int getTotal() {
        return potTotal;
    }

    public void setTotal(int value) {
        potTotal = value;
    }

    public void addPotentialWinner(Player player) {
        potentialWinners.add(player);
    }

    public List<Player> getPotentialWinners() {
        return potentialWinners;
    }

    public void distribute(List<Player> allWinners) {
        List<Player> potWinners = new ArrayList<>();
        for (Player player : allWinners) {
            if (potentialWinners.contains(player)) {
                potWinners.add(player);
            }
        }

        int winnerCount = potWinners.size();
        int winnings = potTotal / winnerCount;
        int remainder = potTotal % winnerCount;
        for (Player player : potWinners) {
            player.setWinnings(winnings);
        }
        if (remainder != 0) {
            Player dealer = table.getDealer();
            Player player = table.getNextPlayer(dealer);
            do {
                while (!potWinners.contains(player)) {
                    Player next = table.getNextPlayer(player);
                    player = next;
                }
                player.setWinnings(player.getWinnings() + 1);
                remainder--;
                Player next = table.getNextPlayer(player);
                player = next;
            } while (remainder > 0);
        }
    }
}