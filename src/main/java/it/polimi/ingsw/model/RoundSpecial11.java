package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Specials.Special11;
import it.polimi.ingsw.model.Specials.Special7;

import java.util.ArrayList;

public class RoundSpecial11 extends RoundStrategy{

    Special11 special;

    public RoundSpecial11(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer, playersInfo);
        special =new Special11();
    }

    public void effect(int color, int playerRef){
        if(useSpecial(special.getCost(), playerRef)) {
            playerManager.setStudentTable(playerRef, color);
            int extracted = bag.extraction();
            special.effect(color, extracted);
        }
    }

}
