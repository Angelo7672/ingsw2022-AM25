package it.polimi.ingsw.server.answer;

import java.util.ArrayList;

public class LoginMessage implements Answer{
    private final String message;
    ArrayList<String> characterAlreadyChosen;

    public LoginMessage(ArrayList<String> characterAlreadyChosen){
        this.message = "Login!";
        this.characterAlreadyChosen = characterAlreadyChosen;
    }

    public String getMessage() {
        return message;
    }
    public ArrayList<String> getCharacterAlreadyChosen() { return characterAlreadyChosen; }
}
