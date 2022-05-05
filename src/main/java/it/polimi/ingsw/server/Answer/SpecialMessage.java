package it.polimi.ingsw.server.Answer;

import it.polimi.ingsw.model.Special;

import java.util.ArrayList;

public class SpecialMessage implements Answer{
    private final ArrayList<Special> specials;
    private final String message;

    public SpecialMessage(ArrayList<Special> specials, String message) {
        this.specials = specials;
        this.message = message;
    }

    public int getCost(int special){
       return specials.get(special).getCost();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
