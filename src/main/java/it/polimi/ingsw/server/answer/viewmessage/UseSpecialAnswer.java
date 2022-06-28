package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

/**
 * UseSpecialAnswer contains info about who played a special character.
 */
public class UseSpecialAnswer implements Answer {
    private final int specialIndex;
    private final int playerRef;

    public UseSpecialAnswer(int playerRef, int specialIndex) {
        this.specialIndex = specialIndex;
        this.playerRef = playerRef;
    }

    public int getSpecialIndex() { return specialIndex; }
    public int getPlayerRef() {
        return playerRef;
    }
}