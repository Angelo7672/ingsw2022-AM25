package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotAllowedException;

public class RoundSpecial4 extends RoundStrategy{
    Special4 special;

    public RoundSpecial4(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager,QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special4();
    }

    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement, int ref) throws NotAllowedException {
        int maxMovement;
        boolean victory = false;
        System.out.println("Ready Increase");
        queueManager.increaseMaxMoveMotherNature(queueRef);
        maxMovement = queueManager.readMaxMotherNatureMovement(queueRef);

        if(desiredMovement > 0 && desiredMovement <= maxMovement){
            islandsManager.moveMotherNature(desiredMovement);
            if(islandsManager.getInhibited(islandsManager.getMotherPos())>0) islandsManager.decreaseInhibited(islandsManager.getMotherPos());
            else victory = conquestIsland(islandsManager.getMotherPos(), -1, -1);
        }else throw new NotAllowedException();

        return victory;
    }

    @Override
    public int getCost(){ return special.getCost(); }
    @Override
    public void increaseCost(){ special.increaseCost(); }
    @Override
    public String getName(){ return special.getName(); }

    private class Special4 extends Special {
        public Special4(){
            super(1, "special4");
        }
    }
}