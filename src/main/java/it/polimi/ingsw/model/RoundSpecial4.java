package it.polimi.ingsw.model;

/**
 * Round strategy used by special 3.
 */
public class RoundSpecial4 extends RoundStrategy{
    Special4 special;

    public RoundSpecial4(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager,QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special4();
    }

    /**
     * @param playerRef is the number of the player. It increases max move mother nature of two additional steps.
     * @return true if effect is used.
     */
    @Override
    public boolean effect(int playerRef) {
        for(int i = 0; i < numberOfPlayer; i++) {
            if (queueManager.readQueue(i) == playerRef) {
                queueManager.increaseMaxMoveMotherNature(i);
            }
        }
        return true;
    }

    @Override
    public void setCost(int cost){ special.setCost(cost); }
    @Override
    public int getCost(){ return special.getCost(); }
    @Override
    public void increaseCost(){ special.increaseCost(); }

    private static class Special4 extends Special {
        public Special4(){
            super(1);
        }
    }
}