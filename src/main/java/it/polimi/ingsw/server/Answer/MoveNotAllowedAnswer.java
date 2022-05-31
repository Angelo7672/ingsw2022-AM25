package it.polimi.ingsw.server.Answer;

public class MoveNotAllowedAnswer implements Answer{
    private final String message;

    public MoveNotAllowedAnswer() {
        this.message = "move not allowed";
    }

    public String getMessage() {
        return message;
    }
}
