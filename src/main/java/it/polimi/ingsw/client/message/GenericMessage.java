package it.polimi.ingsw.client.message;

/**
 * GenericMessage is a generic message with just a string.
 */
public class GenericMessage implements Message {
    private final String message;

    public GenericMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
