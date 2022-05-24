package it.polimi.ingsw.server.Answer;

import java.util.ArrayList;

public class CardsMessage implements Answer{
    private final ArrayList<String> playedCards;

    public CardsMessage(ArrayList<String> playedCards) {
        this.playedCards = playedCards;
    }

    public ArrayList<String> getPlayedCards() { return playedCards; }
    @Override
    public String getMessage() { return null; }
}
