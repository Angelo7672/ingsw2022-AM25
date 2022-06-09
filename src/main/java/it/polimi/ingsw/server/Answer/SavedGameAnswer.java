package it.polimi.ingsw.server.Answer;

import java.util.List;

public class SavedGameAnswer implements Answer{
    private int numberOfPlayers;
    private boolean expertMode;

    public SavedGameAnswer(List<Integer> lastGame) {
        this.numberOfPlayers = lastGame.get(0);
        if(lastGame.get(1) == 0) this.expertMode = false;
        else if(lastGame.get(1) == 1) this.expertMode = true;
    }

    public int getNumberOfPlayers() { return numberOfPlayers; }
    public boolean isExpertMode() { return expertMode; }
}
