package it.polimi.ingsw.client.Message;

public class PingMessage implements Message {
    private String message;

    public PingMessage(String message){
        this.message = message;
    }

    private String getMessage(){
        return message;
    }
}
