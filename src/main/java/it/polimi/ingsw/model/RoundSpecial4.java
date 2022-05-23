package it.polimi.ingsw.model;

public class RoundSpecial4 extends RoundStrategy{

    Special4 special;

    public RoundSpecial4(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager,QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special4();
    }


    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement, int ref){
        int maxMovement;
        boolean victory = false;

        maxMovement = queueManager.readMaxMotherNatureMovement(queueRef)+2; //da incrementare anche per scelta del giocatore

        if(desiredMovement > 0 && desiredMovement <= maxMovement){
            islandsManager.moveMotherNature(desiredMovement);
            if(islandsManager.getInhibited(islandsManager.getMotherPos())>0){
                islandsManager.decreaseInhibited(islandsManager.getMotherPos());
            }
            else {
                victory = conquestIsland(islandsManager.getMotherPos(), -1, -1);
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
