package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.*;

import java.util.ArrayList;


public class RoundSpecial1 extends RoundStrategy{

    Special1 special;
    
    public RoundSpecial1(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer, playersInfo);
        special = new Special1();
    }

    public void effect(int color, int islandRef, int playerRef){
        if(useSpecial(special.getCost(), playerRef)) {
            islandsManager.incStudent(islandRef, color);
            int extracted = bag.extraction();
            special.effect(color, extracted);
        }
    }

}
