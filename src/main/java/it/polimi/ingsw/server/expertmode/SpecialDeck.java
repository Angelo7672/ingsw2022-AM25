package it.polimi.ingsw.server.expertmode;

import it.polimi.ingsw.server.Entrance;

import java.util.ArrayList;
import java.util.List;

public class SpecialDeck {
    private List<Special> specialsOfThisMatch;

    public SpecialDeck(){
        this.specialsOfThisMatch = new ArrayList<Special>();
    }

    public void addSpecial(CreateSpecial createSpecial, Entrance server){ specialsOfThisMatch.add(createSpecial.makeSpecial(server)); }
}