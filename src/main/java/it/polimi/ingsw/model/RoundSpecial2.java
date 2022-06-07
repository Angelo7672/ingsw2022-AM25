package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotAllowedException;

public class RoundSpecial2 extends RoundStrategy {
    Special2 special;

    public RoundSpecial2(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special2();
    }

    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException {    //da cambiare e spezzare in due
        if(!inSchool){
            if(islandRef < 0 || islandRef >= islandsManager.getIslandsSize()) throw new NotAllowedException();
            playerManager.transferStudent(playerRef, colour, inSchool, true);
            islandsManager.incStudent(islandRef,colour,1);
        }
        else if(inSchool)
            playerManager.transferStudent(playerRef, colour, inSchool, true);
    }

    @Override
    public int getCost(){
        return special.getCost();
    }
    @Override
    public void increaseCost(){
        special.increaseCost();
    }
    @Override
    public String getName(){
        return special.getName();
    }

    private class Special2 extends Special{
        public Special2(){
            super(2, "special2");
        }
    }
}