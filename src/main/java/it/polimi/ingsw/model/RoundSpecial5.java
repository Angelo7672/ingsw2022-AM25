package it.polimi.ingsw.model;

import java.util.ArrayList;

public class RoundSpecial5 extends RoundStrategy{

    Special5 special;
    ArrayList<Integer> null1;
    ArrayList<Integer> null2;


    public RoundSpecial5(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer,playersInfo, cloudsManager, islandsManager, playerManager, bag);
        special = new Special5();
    }

    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement, int noColor, int islandRef) {
        int maxMovement;
        boolean victory = false;
        effect(islandRef, null1, null2);

        maxMovement = playerManager.readMaxMotherNatureMovement(queueRef);
        if (desiredMovement > 0 && desiredMovement <= maxMovement) {
            islandsManager.moveMotherNature(desiredMovement);
            if (islandsManager.getInhibited(islandsManager.getMotherPos())>0) {
                islandsManager.decreaseInhibited(islandsManager.getMotherPos());
            } else {
                victory = conquestIsland(islandsManager.getMotherPos(), noColor, queueRef);
            }
        }
        return victory;
    }

    @Override
    public boolean effect(int islandRef, ArrayList<Integer>null1, ArrayList<Integer> null2){
        if(special.getNoEntry()>0){
            islandsManager.increaseInhibited(islandRef);
            special.decreaseNoEntry();
            return true;
        }
        return false;
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

    @Override
    public void effect(){
        int inhibitedIsland = 0;
        for (int i = 0; i<islandsManager.size(); i++){
            if(islandsManager.getInhibited(i)>0) inhibitedIsland+=islandsManager.getInhibited(i);
        }
        if(inhibitedIsland<(4-special.getNoEntry())) special.increaseNoEntry();
    }

    private class Special5 extends Special{
        private int noEntry;

        public Special5(){
            super(2, "special5");
            noEntry=4;
        }


        public int getNoEntry(){
            return noEntry;
        }

        public void increaseNoEntry(){
            noEntry+=1;
        }

        public void decreaseNoEntry(){
            noEntry-=1;
        }

    }




}
