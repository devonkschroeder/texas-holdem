package hannahschroeder.texasholdem;

import java.util.ArrayList;
import java.util.List;

class Pot {
    Table table;
    int potTotal;
    List<Player> potentialWinners = new ArrayList<>();
    List<Player> winners = new ArrayList<>();

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

    public void addToPot(int value) {
        potTotal += value;
    }

    public void addPotentialWinner(Player player) {
        potentialWinners.add(player);
    }

    public void removePotentialWinner (Player player) {
        potentialWinners.remove(player);
    }

    public List<Player> getPotentialWinners() {
        return potentialWinners;
    }

    public List<Player> getWinners() {
        return winners;
    }

    public void distribute() {
        int winnerCount = winners.size();
        int winnings = potTotal / winnerCount;
        int remainder = potTotal % winnerCount;
        for (Player player : winners) {
            player.addToWinnings(winnings);
        }
        if (remainder != 0) {
            Player dealer = table.getDealer();
            Player player = table.getNextPlayer(dealer);
            do {
                while (!winners.contains(player)) {
                    Player next = table.getNextPlayer(player);
                    player = next;
                }
                player.addToWinnings(1);
                remainder--;
                Player next = table.getNextPlayer(player);
                player = next;
            } while (remainder > 0);
        }
    }
}