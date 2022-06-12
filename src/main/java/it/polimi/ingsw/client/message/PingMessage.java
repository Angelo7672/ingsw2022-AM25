package it.polimi.ingsw.client.message;

public class PingMessage implements Message {
    private String message;

    public PingMessage(){
        this.message = "ping";
    }

    private String getMessage(){
        return message;
    }
}
