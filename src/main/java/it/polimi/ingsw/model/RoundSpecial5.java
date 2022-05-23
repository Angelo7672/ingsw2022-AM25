package it.polimi.ingsw.model;

import java.util.ArrayList;

public class RoundSpecial5 extends RoundStrategy{

    Special5 special;
    ArrayList<Integer> null1;
    ArrayList<Integer> null2;


    public RoundSpecial5(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special5();
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
