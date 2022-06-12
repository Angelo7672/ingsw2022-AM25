package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class NumberOfCardsMessage implements Answer {
    private final int numberOfCards;
    private final int playerRef;

    public NumberOfCardsMessage(int numberOfCards, int playerRef) {
        this.numberOfCards = numberOfCards;
        this.playerRef = playerRef;
    }


    public int getNumberOfCards() {
        return numberOfCards;
    }

    public int getPlayerRef() {
        return playerRef;
    }
}
