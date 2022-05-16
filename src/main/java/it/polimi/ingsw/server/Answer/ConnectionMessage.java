package it.polimi.ingsw.server.Answer;

public class ConnectionMessage implements Answer{
    private final String chosenNickname;
    private final String Character;
    private final String message;

    public ConnectionMessage(String chosenNickname, String remainingCharacter, String message) {
        this.chosenNickname = chosenNickname;
        this.Character = remainingCharacter;
        this.message = message;
    }

    public String getCharacter() {
        return Character;
    }

    public String getChosenNickname() {
        return chosenNickname;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
