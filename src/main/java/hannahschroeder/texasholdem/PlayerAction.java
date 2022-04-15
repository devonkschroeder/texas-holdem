package hannahschroeder.texasholdem;

import java.util.EnumSet;
import java.util.Scanner;

public enum PlayerAction {
    PASS("pass"),
    ALL_IN("all in"),
    BET("bet"),
    RAISE("raise"),
    CALL("call"),
    CHECK("check"),
    FOLD("fold");

    private String text;

    PlayerAction(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }

    private PlayerAction() {
    }

    public static PlayerAction getAction(String text) {
        for (PlayerAction action : PlayerAction.values()) {
            if (action.text.equals(text)) {
                return action;
            }
        }
        return null;
    }

    public static PlayerAction getActionFromScanner(Scanner in, Player player, EnumSet<PlayerAction> validActions, int betsTotal, int currentBet) {
        PlayerAction action = null;
        if (validActions == null) {
            return PlayerAction.PASS;
        }

        boolean isValidAction;
        do {
            String playerInfo = String.format("%s (%s %d)", player.getName(), player.getPrivateHand().toString(), player.getStackValue());
            System.out.printf("Bets: %d, Current bet: %d%n", betsTotal, currentBet);
            System.out.printf("%s, what would you like to do? ", playerInfo);
            printEnumSet(validActions);
            action = PlayerAction.getAction(in.nextLine().toLowerCase());
            if (action == null || !validActions.contains(action)) {
                isValidAction = false;
                System.out.println("Invalid action");
            } else {
                isValidAction = true;
            }
        } while (!isValidAction);
        return action;
    }

    public static void printEnumSet(EnumSet<PlayerAction> set) {
        String setString = "(";
        for (PlayerAction action : set) {
            setString = setString + action.toString() + ", ";
        }
        setString = setString.substring(0, setString.length() - 2);
        setString += ")";
        System.out.println(setString);
    }
}