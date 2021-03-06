package it.polimi.ingsw.server.answer;

import java.util.List;

/**
 * SavedGameAnswer contains info about the number of players and the game mode of the last saved game.
 */
public class SavedGameAnswer implements Answer{
    private final int numberOfPlayers;
    private boolean expertMode;

    /**
     * Create an answer containing info about the number of players and the game mode of the last saved game.
     * @param lastGame is a list with in first position the number of players, and in second position the game mode ( 0 -> normal, 1 -> expert);
     */
    public SavedGameAnswer(List<Integer> lastGame) {
        this.numberOfPlayers = lastGame.get(0);
        if(lastGame.get(1) == 0) this.expertMode = false;
        else if(lastGame.get(1) == 1) this.expertMode = true;
    }

    public int getNumberOfPlayers() { return numberOfPlayers; }
    public boolean isExpertMode() { return expertMode; }
}
