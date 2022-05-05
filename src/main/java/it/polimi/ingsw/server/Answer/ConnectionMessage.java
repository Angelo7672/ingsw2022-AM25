package it.polimi.ingsw.server.Answer;

import java.util.ArrayList;

public class ConnectionMessage implements Answer{
    private final ArrayList<String> chosenNickname;
    private final ArrayList<String> remainingCharacter;
    private final String message;

    public ConnectionMessage(ArrayList<String> chosenNickname, ArrayList<String> remainingCharacter, String message) {
        this.chosenNickname = chosenNickname;
        this.remainingCharacter = remainingCharacter;
        this.message = message;
    }

    public ArrayList<String> getRemainingCharacter() {
        return remainingCharacter;
    }

    public ArrayList<String> getChosenNickname() {
        return chosenNickname;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
