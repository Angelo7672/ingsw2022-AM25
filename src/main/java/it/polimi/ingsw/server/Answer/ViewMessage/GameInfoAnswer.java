package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class GameInfoAnswer implements Answer {
    private final int numberOfPlayers;
    private final boolean isExpertMode;

    public GameInfoAnswer(int numberOfPlayers, boolean isExpertMode) {
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
