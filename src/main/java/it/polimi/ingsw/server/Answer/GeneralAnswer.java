package it.polimi.ingsw.server.Answer;

public class GeneralAnswer implements Answer{
    private final String message;

    public GeneralAnswer(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
