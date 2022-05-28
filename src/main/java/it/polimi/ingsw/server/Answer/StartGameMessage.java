package it.polimi.ingsw.server.Answer;

public class StartGameMessage implements Answer{
    private final int numberOfPlayers;
    private final boolean isExpertMode;

    public StartGameMessage(int numberOfPlayers, boolean isExpertMode) {
        this.numberOfPlayers = numberOfPlayers;
        this.isExpertMode = isExpertMode;
    }

    @Override
    public String getMessage() {
        return null;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public boolean isExpertMode() {
        return isExpertMode;
    }
}
