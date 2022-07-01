package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * UseSpecialAnswer contains info about who played a special character.
 */
public class UseSpecialAnswer implements Answer {
    private final int specialIndex;
    private final int playerRef;

    /**
     * Create an answer contains info about who played a special character.
     * @param playerRef player who played special;
     * @param specialIndex special played;
     */
    public UseSpecialAnswer(int playerRef, int specialIndex) {
        this.specialIndex = specialIndex;
        this.playerRef = playerRef;
    }

    public int getSpecialIndex() { return specialIndex; }
    public int getPlayerRef() {
        return playerRef;
    }
}