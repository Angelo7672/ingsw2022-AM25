package it.polimi.ingsw.server.Answer;

public class SetupGameMessage implements Answer{
    private final String message;

    public SetupGameMessage(){
        this.message = "Welcome! You are the first! How many players?\nExpert mode yes or no?";
    }

    @Override
    public String getMessage() {
        return null;
    }
}
