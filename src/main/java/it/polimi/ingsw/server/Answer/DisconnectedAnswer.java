package it.polimi.ingsw.server.Answer;

public class DisconnectedAnswer implements Answer{
    private final String message;

    public DisconnectedAnswer(){ this.message = "Socket Close"; }

    @Override
    public String getMessage() {
        return message;
    }
}