package it.polimi.ingsw.model.Islands;

import it.polimi.ingsw.model.RoundStrategy;
import it.polimi.ingsw.model.Specials.*;

import java.util.ArrayList;

public class RoundSpecial2 extends RoundStrategy {

    public RoundSpecial2(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer,playersInfo);
        special = new Special2();
    }

    private ArrayList<Integer> profOwner(){

    }


}
