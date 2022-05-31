package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class LastCardMessage implements Answer {
    private final int playerRef;
    private String card;

    public LastCardMessage(int playerRef, String card) {
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
