package hannahschroeder.texasholdem;

public enum Stage {
    PREFLOP,
    FLOP,
    TURN,
    RIVER;

	public Stage nextStage() {
		switch (this) {
            case PREFLOP:
                return FLOP;
            case FLOP:
                return TURN;
            case TURN:
                return RIVER;
            case RIVER:
                return RIVER;
            default:
                throw new RuntimeException("Unexpected stage");
        }
	}
}