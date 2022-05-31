package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

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
