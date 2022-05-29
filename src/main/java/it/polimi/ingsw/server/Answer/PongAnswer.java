package it.polimi.ingsw.server.Answer;

public class PongAnswer implements Answer{
    private final String message;

    public PongAnswer(){ this.message = "pong"; }

    @Override
    public String getMessage() {
        return message;
    }
}
