package it.polimi.ingsw.server.Answer;

public class SetupGameMessage implements Answer{
    private final String message;

    public SetupGameMessage(String message){

        this.message = message;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
