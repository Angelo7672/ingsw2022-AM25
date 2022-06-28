package it.polimi.ingsw.server.answer;

/**
 * PongAnswer is the answer of the server to ping message.
 */
public class PongAnswer implements Answer{
    private final String message;

    public PongAnswer(){ this.message = "pong"; }

    public String getMessage() {
        return message;
    }
}
