package it.polimi.ingsw.model;

public class RoundSpecial3 extends RoundStrategy{

    Special3 special;

    public RoundSpecial3(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer,playersInfo, cloudsManager, islandsManager, playerManager, bag);
        special = new Special3();
    }

    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement, int noColor, int islandRef){
        int maxMovement;
        boolean victory1 = false;
        boolean victory2 = false;

        victory1 = specialConquestIsland(islandRef, -1, queueRef);

        maxMovement = playerManager.readMaxMotherNatureMovement(queueRef);
        if(desiredMovement > 0 && desiredMovement <= maxMovement ){
            islandsManager.moveMotherNature(desiredMovement);
            if(islandsManager.getInhibited(islandsManager.getMotherPos())>0){
                islandsManager.decreaseInhibited(islandsManager.getMotherPos());
            }
            else {
                victory2 = conquestIsland(islandsManager.getMotherPos(), -1, queueRef);
            }
        }

        if(victory1 || victory2 ) return true;
        return false;
    }

    private boolean specialConquestIsland(int islandRef, int noColor, int playerRef){
        boolean victory = false;
        if(islandsManager.getInhibited(islandRef)>0){
            islandsManager.decreaseInhibited(islandRef);
        }
        else {
            victory = conquestIsland(islandsManager.getMotherPos(), noColor, playerRef);
        }
        return victory;
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

    private class Special3 extends Special {

        //non fa niente qua, fa solo calcolare l'influenza di un isola a scelta
        public Special3(){
            super(3, "special3");
        }

    }

}
