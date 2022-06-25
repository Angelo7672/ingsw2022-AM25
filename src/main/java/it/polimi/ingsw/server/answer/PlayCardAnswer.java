package it.polimi.ingsw.server.answer;

public class PlayCardAnswer implements Answer{
    private final String message;

    public PlayCardAnswer() {
        this.message = "Play card!";
    }

    public String getMessage() {
        return message;
    }
}

