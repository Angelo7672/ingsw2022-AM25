package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special;
import it.polimi.ingsw.model.Specials.Special1;
import it.polimi.ingsw.model.Specials.Special5;

import java.util.ArrayList;

public class RoundSpecial5 extends RoundStrategy{

    Special5 special;

    public RoundSpecial5(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer,playersInfo);
        special = new Special5();
    }

    public void effect(int islandRef, int playerRef){
        if(special.getNoEntry()>0){
            if(useSpecial(special.getCost(), playerRef)) {
                islandsManager.setInhibited(islandRef, true);
                special.decreaseNoEntry();
            }
        }
    }


}
