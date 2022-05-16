package it.polimi.ingsw.server.Answer;

public class StartTurn implements Answer{
    private final String message;

    public StartTurn() {
        this.message = "Start your Action Phase!";
    }

    @Override
    public String getMessage() {
        return message;
    }

}
