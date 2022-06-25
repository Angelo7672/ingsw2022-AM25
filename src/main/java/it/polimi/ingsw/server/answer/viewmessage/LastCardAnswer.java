package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class LastCardAnswer implements Answer {
    private final int playerRef;
    private String card;

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
