package it.polimi.ingsw.client.Message;

import it.polimi.ingsw.server.Answer.Answer;

public class GenericMessage implements Message {
    private final String message;

    public GenericMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
