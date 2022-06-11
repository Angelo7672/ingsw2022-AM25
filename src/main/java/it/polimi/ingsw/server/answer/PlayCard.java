package it.polimi.ingsw.server.answer;

public class PlayCard implements Answer{
    private final String message;

    public PlayCard() {
        this.message = "Play card!";
    }

    public String getMessage() {
        return message;
    }
}

