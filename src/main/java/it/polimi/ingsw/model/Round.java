package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Round extends RoundStrategy{

    public Round(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer,playersInfo, cloudsManager, islandsManager, playerManager, bag);
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


    @Override
    public void chooseCloud(int playerRef,int cloudRef){
        for(int i = 0; i < 5 ; i++) playerManager.setStudentEntrance(playerRef, cloudsManager.removeStudents(cloudRef)[i]);
    }
}