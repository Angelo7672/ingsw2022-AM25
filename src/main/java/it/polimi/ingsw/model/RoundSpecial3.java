package it.polimi.ingsw.model;

/**
 * Round strategy used by special 3.
 */
public class RoundSpecial3 extends RoundStrategy{
    Special3 special;

    public RoundSpecial3(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special3();
    }

    /**
     * It checks if effect could be used, then use it.
     * @param islandRef is the number of the island.
     * @return true if effect is used.
     */
    @Override
    public boolean effect(int islandRef){
        boolean victory = false;
        if(islandsManager.getInhibited(islandRef)>0) islandsManager.decreaseInhibited(islandRef);
        else victory = conquestIsland(islandRef, -1, -1);

        return victory;
    }

    @Override
    public void setCost(int cost){ special.setCost(cost); }
    @Override
    public int getCost(){ return special.getCost(); }
    @Override
    public void increaseCost(){ special.increaseCost(); }

    private static class Special3 extends Special {
        //non fa niente qua, fa solo calcolare l'influenza di un isola a scelta
        public Special3(){
            super(3);
        }
    }
}