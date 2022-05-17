package it.polimi.ingsw.server.Answer;

import java.util.ArrayList;

public class ConnectionMessage implements Answer{
    private final ArrayList<String> chosenNickname;
    private final ArrayList<String> remainingCharacter;
    private final String message;
    private final boolean isExpert;
    private final int numberOfPlayers;


    public ConnectionMessage(ArrayList<String> chosenNickname, ArrayList<String> remainingCharacter, String message, boolean isExpert, int numberOfPlayers) {
        this.chosenNickname = chosenNickname;
        this.remainingCharacter = remainingCharacter;
        this.message = message;
        this.isExpert = isExpert;
        this.numberOfPlayers = numberOfPlayers;
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

    public boolean isExpert() {
        return isExpert;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}
