package it.polimi.ingsw.model;

public class RoundSpecial3 extends RoundStrategy{
    Special3 special;

    public RoundSpecial3(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special3();
    }

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

    private class Special3 extends Special {
        //non fa niente qua, fa solo calcolare l'influenza di un isola a scelta
        public Special3(){
            super(3, "special3");
        }
    }
}