package it.polimi.ingsw.client.message;

/**
 * PingMessage is sent to server every 5 seconds to check if server is online.
 */
public class PingMessage implements Message {
    private String message;

    public PingMessage(){
        this.message = "ping";
    }

    private String getMessage(){
        return message;
    }
}
