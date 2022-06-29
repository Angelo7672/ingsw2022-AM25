package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotAllowedException;

public class RoundSpecial4 extends RoundStrategy{
    Special4 special;

    public RoundSpecial4(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager,QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special4();
    }

    @Override
    public boolean effect(int playerRef) {
        for(int i = 0; i < numberOfPlayer; i++)
            if(queueManager.readQueue(i) == playerRef) queueManager.increaseMaxMoveMotherNature(i);
        return true;
    }

    @Override
    public void setCost(int cost){ special.setCost(cost); }
    @Override
    public int getCost(){ return special.getCost(); }
    @Override
    public void increaseCost(){ special.increaseCost(); }

    private class Special4 extends Special {
        public Special4(){
            super(1, "special4");
        }
    }
}