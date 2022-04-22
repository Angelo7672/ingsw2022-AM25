package it.polimi.ingsw.model;

public class RoundSpecial4 extends RoundStrategy{

    Special4 special;

    public RoundSpecial4(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer,playersInfo, cloudsManager, islandsManager, playerManager, bag);
        special = new Special4();
    }


    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement, int noColor, int islandRef){
        int maxMovement;
        boolean victory = false;

        maxMovement = playerManager.readMaxMotherNatureMovement(queueRef)+2; //da incrementare anche per scelta del giocatore

        if(desiredMovement > 0 && desiredMovement <= maxMovement){
            islandsManager.moveMotherNature(desiredMovement);
            if(islandsManager.getInhibited(islandsManager.getMotherPos())){
                islandsManager.setInhibited(islandsManager.getMotherPos(), false);
            }
            else {
                victory = conquestIsland(islandsManager.getMotherPos(), noColor, queueRef);
            }
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

    private class Special4 extends Special {
        //non fa niente qua, fa solo muovere madre natura di due passi in piu
        public Special4(){
            super(1, "special4");
        }
    }
}
