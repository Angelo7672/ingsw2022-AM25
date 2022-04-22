package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Round extends RoundStrategy{

    public Round(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer,playersInfo, cloudsManager, islandsManager, playerManager, bag);
    }

    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef){
        if(!inSchool){
            playerManager.transferStudent(playerRef, colour, inSchool, false);
            islandsManager.incStudent(islandRef,colour);
        }
        else if(inSchool)
            playerManager.transferStudent(playerRef, colour, inSchool, false);
    }

    @Override
    public int getCost(){
        return 0;
    }
    @Override
    public void increaseCost(){}

    @Override
    public String getName() {
        return null;
    }

}