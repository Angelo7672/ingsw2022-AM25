package it.polimi.ingsw.server.Answer;



import java.util.ArrayList;

public class CardsMessage implements Answer{
    public final ArrayList<String> cards;
    public final String message;

    public CardsMessage(ArrayList<String> cards, String message) {
        this.cards = cards;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
