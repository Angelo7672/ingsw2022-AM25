package it.polimi.ingsw.server.Answer;

import it.polimi.ingsw.model.Special;

import java.util.ArrayList;

public class SpecialMessage {
    private final ArrayList<Special> specials;

    public SpecialMessage(ArrayList<Special> specials) {
        this.specials = specials;
    }

    public int getCost(int special){
       return specials.get(special).getCost();
    }
}
