package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

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
}
