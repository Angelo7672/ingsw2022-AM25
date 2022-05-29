package it.polimi.ingsw.server.Answer;

public class SoldOutAnswer implements Answer{
    private final String message;

    public SoldOutAnswer(){ this.message = "Server Sold Out"; }

    @Override
    public String getMessage() {
        return message;
    }
}
