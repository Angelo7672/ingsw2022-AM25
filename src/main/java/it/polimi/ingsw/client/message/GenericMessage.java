package it.polimi.ingsw.client.Message;

public class GenericMessage implements Message {
    private final String message;

    public GenericMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
