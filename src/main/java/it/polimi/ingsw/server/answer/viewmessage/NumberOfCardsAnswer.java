package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class NumberOfCardsAnswer implements Answer {
    private final int numberOfCards;
    private final int playerRef;

    public NumberOfCardsAnswer(int numberOfCards, int playerRef) {
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
