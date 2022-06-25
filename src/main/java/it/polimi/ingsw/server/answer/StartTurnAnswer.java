package it.polimi.ingsw.server.answer;

public class StartTurnAnswer implements Answer{
    private final String message;

    public StartTurnAnswer() {
        this.message = "Start your Action Phase!";
    }

    public String getMessage() {
        return message;
    }

}
