package it.polimi.ingsw.server.Answer;

import java.util.ArrayList;

public class LoginMessage implements Answer{
    private final String message;
    ArrayList<String> characterAlreadyChosen;

    public LoginMessage(ArrayList<String> characterAlreadyChosen){
        this.message = "Login!";
        this.characterAlreadyChosen = characterAlreadyChosen;
    }

    @Override
    public String getMessage() {
        return message;
    }
    public ArrayList<String> getCharacterAlreadyChosen() { return characterAlreadyChosen; }
}
