package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotAllowedException;

/**
 * Round strategy used by special 2, it overrides moveStudent and use its effect.
 */
public class RoundSpecial2 extends RoundStrategy {
    Special2 special;

    public RoundSpecial2(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special2();
    }


    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef) throws NotAllowedException {
        if(!inSchool){
            if(islandRef < 0 || islandRef >= islandsManager.getIslandsSize()) throw new NotAllowedException();
            playerManager.transferStudent(playerRef, colour, inSchool, true);
            islandsManager.incStudent(islandRef,colour,1);
        }
        else if(inSchool)
            playerManager.transferStudent(playerRef, colour, inSchool, true);
    }

    @Override
    public void setCost(int cost){ special.setCost(cost); }
    @Override
    public int getCost(){
        return special.getCost();
    }
    @Override
    public void increaseCost(){
        special.increaseCost();
    }

    private static class Special2 extends Special{
        public Special2(){
            super(2);
        }
    }
}