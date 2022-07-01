package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * LastCardAnswer contains info about the last card played from a player.
 */
public class LastCardAnswer implements Answer {
    private final int playerRef;
    private final String card;

    public LastCardAnswer(int playerRef, String card) {
        this.playerRef = playerRef;
        this.card = card;
    }

    public String getCard() {
        return card;
    }
    public int getPlayerRef() {
        return playerRef;
    }
}
