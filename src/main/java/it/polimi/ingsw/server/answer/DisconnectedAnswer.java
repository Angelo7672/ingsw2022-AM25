package it.polimi.ingsw.server.answer;

public class DisconnectedAnswer implements Answer{
    private final String message;

    public DisconnectedAnswer(){ this.message = "Socket Close"; }

    public String getMessage() {
        return message;
    }
}