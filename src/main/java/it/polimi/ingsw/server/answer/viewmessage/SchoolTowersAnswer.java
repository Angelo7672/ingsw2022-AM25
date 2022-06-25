package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class SchoolTowersAnswer implements Answer {
    private final int playerRef;
    private final int towers;

    public SchoolTowersAnswer(int playerRef, int towers) {
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
