package it.polimi.ingsw.server.answer;

/**
 * DisconnectedAnswer said to a client that the server will close socket soon.
 */
public class DisconnectedAnswer implements Answer{
    private final String message;

    public DisconnectedAnswer(){ this.message = "Socket Close"; }

    public String getMessage() { return message; }
}