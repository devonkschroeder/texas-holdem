package hannahschroeder.texasholdem;

import java.util.ArrayList;
import java.util.List;

public class Playerlist {
    private List<Player> players;
    
    public Playerlist() {
        players = new ArrayList<>();
    }

    public void addPlayer(String playerName, int startCash) {
        players.add(new Player(playerName, players.size(), startCash));
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public Player getPlayerById(int id) {
        return getPlayers().get(id);
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

    public Player getNextActivePlayer(Player previousPlayer) {
        Player nextPlayer;

        do {
            nextPlayer = getNextPlayer(previousPlayer);
            previousPlayer = nextPlayer;
        } while (nextPlayer.isFolded() || nextPlayer.isBusted());

        return nextPlayer;
    }

    public List<Player> getActivePlayers() {
        List<Player> activePlayers = new ArrayList<>();
        for (Player player : getPlayers()) {
            if (!player.isFolded() && !player.isBusted()) {
                activePlayers.add(player);
            }
        }
        return activePlayers;
    }
}
