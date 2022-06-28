package it.polimi.ingsw.server.answer;

/**
 * GenericAnswer is a generic answer contains only a string message.
 */
public class GenericAnswer implements Answer{
    private final String message;

    public GenericAnswer(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}
