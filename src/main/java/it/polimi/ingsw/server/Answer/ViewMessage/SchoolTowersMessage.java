package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class SchoolTowersMessage implements Answer {
    private final int playerRef;
    private final int towers;

    public SchoolTowersMessage(int playerRef, int towers) {
        this.playerRef = playerRef;
        this.towers = towers;
    }

    public int getPlayerRef() {
        return playerRef;
    }
    public int getTowers() {
        return towers;
    }
    @Override
    public String getMessage() {
        return null;
    }
}
