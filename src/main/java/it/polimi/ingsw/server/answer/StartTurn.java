package it.polimi.ingsw.server.answer;

public class StartTurn implements Answer{
    private final String message;

    public StartTurn() {
        this.message = "Start your Action Phase!";
    }

    public String getMessage() {
        return message;
    }

}
