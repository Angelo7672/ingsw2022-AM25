package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class UseSpecialAnswer implements Answer {
    private final int specialIndex;
    private final int playerRef;

    public UseSpecialAnswer(int playerRef, int specialIndex) {
        this.playerRef = playerRef;
        this.specialIndex = specialIndex;
    }

    public int getSpecialIndex() { return specialIndex; }
    public int getPlayerRef() { return playerRef; }
}