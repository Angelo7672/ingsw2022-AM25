package it.polimi.ingsw.server.Answer;

public class PlayCard implements Answer{
    private final String message;

    public PlayCard() {
        this.message = "Play card!";
    }

    @Override
    public String getMessage() {
        return message;
    }
}

