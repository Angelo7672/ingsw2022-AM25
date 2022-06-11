package it.polimi.ingsw.server.answer;

public class GenericAnswer implements Answer{
    private final String message;

    public GenericAnswer(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}
