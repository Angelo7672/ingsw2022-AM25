package it.polimi.ingsw.server.Answer;



import java.util.ArrayList;

public class CardsMessage implements Answer{
    private final ArrayList<String> playedCards;
    private final ArrayList<String> hand;
    private final String message;

    public CardsMessage(ArrayList<String> playedCards, ArrayList<String> hand, String message) {
        this.playedCards = playedCards;
        this.hand = hand;
        this.message = message;
    }

    public ArrayList<String> getHand() {
        return hand;
    }

    public ArrayList<String> getPlayedCards() {
        return playedCards;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
