package it.polimi.ingsw.server.answer;

/**
 * SoldOutAnswer comunicate to the client that are nor available connections for this game and the socket will be close soon.
 */
public class SoldOutAnswer implements Answer{
    private final String message;

    /**
     * Create an answer contains the message "Server Sold Out".
     */
    public SoldOutAnswer(){ this.message = "Server Sold Out"; }

    public String getMessage() { return message; }
}