package it.polimi.ingsw.server.answer;

import java.util.ArrayList;

public class LoginAnswer implements Answer{
    private final String message;
    ArrayList<String> characterAlreadyChosen;

    public LoginAnswer(ArrayList<String> characterAlreadyChosen){
        this.message = "Login!";
        this.characterAlreadyChosen = characterAlreadyChosen;
    }

    public String getMessage() {
        return message;
    }
    public ArrayList<String> getCharacterAlreadyChosen() { return characterAlreadyChosen; }
}
