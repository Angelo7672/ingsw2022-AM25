package it.polimi.ingsw.server.answer;

public class PongAnswer implements Answer{
    private final String message;

    public PongAnswer(){ this.message = "pong"; }

    public String getMessage() {
        return message;
    }
}
