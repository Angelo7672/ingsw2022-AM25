package it.polimi.ingsw.model;

import it.polimi.ingsw.model.RoundStrategy;
import it.polimi.ingsw.model.Specials.*;
import it.polimi.ingsw.model.Team;

import java.util.ArrayList;

public class RoundSpecial2 extends RoundStrategy {

    Special2 special;

    public RoundSpecial2(int numberOfPlayer, String[] playersInfo){
        super(numberOfPlayer,playersInfo);
        special = new Special2();
    }

    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef){
        if(!inSchool){
            playerManager.transferStudent(playerRef, colour, inSchool, true);
            islandsManager.incStudent(islandRef,colour);
        }
        else if(inSchool)
            playerManager.transferStudent(playerRef, colour, inSchool, true);
    }


}
