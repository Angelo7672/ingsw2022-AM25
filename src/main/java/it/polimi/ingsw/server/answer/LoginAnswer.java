package it.polimi.ingsw.server.answer;

import java.util.ArrayList;

/**
 * LoginAnswer contains info about the already chosen characters from the other players, and a message that invite client to procede with login.
 */
public class LoginAnswer implements Answer{
    private final String message;
    ArrayList<String> characterAlreadyChosen;

    /**
     * Create an answer contains already chosen characters.
     * @param characterAlreadyChosen already chosen characters;
     */
    public LoginAnswer(ArrayList<String> characterAlreadyChosen){
        this.message = "Login!";
        this.characterAlreadyChosen = characterAlreadyChosen;
    }

    public String getMessage() { return message; }
    public ArrayList<String> getCharacterAlreadyChosen() { return characterAlreadyChosen; }
}
