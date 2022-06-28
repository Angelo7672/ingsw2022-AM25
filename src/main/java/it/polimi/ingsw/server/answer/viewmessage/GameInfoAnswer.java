package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

/**
 * GameInfoAnswer contains info about the current game.
 */
public class GameInfoAnswer implements Answer {
    private final int numberOfPlayers;
    private final boolean isExpertMode;

    public GameInfoAnswer(int numberOfPlayers, boolean isExpertMode) {
        this.numberOfPlayers = numberOfPlayers;
        this.isExpertMode = isExpertMode;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public boolean isExpertMode() {
        return isExpertMode;
    }
}
