import java.util.Scanner;

public enum PlayerAction {
    BET_OR_RAISE,
    CALL_OR_CHECK,
    FOLD;

    private PlayerAction() {
    }

    public static PlayerAction getAction(String text) {
        if (text.equals("raise") || text.equals("bet")) {
            return PlayerAction.BET_OR_RAISE;
        } else if (text.equals("call") || text.equals("check")) {
            return PlayerAction.CALL_OR_CHECK;
        } else if (text.equals("fold")) {
            return PlayerAction.FOLD;
        }
        return null;
    }

    public static PlayerAction getActionFromScanner(Scanner in, Player player) {        // get input from player
        PlayerAction action = null;
        while (action == null) {
            System.out.printf("%s (%s %d), ", player.getName(), player.getPrivateHand(), player.stackValue());
            System.out.println("what would you like to do? (raise, bet, call, check fold)");
            action = PlayerAction.getAction(in.nextLine().toLowerCase());
        }
        return action;
    }
}