package it.polimi.ingsw.model;


import it.polimi.ingsw.listeners.NoEntryListener;

public class RoundSpecial5 extends RoundStrategy{
    Special5 special;
    NoEntryListener noEntryListener;

    public RoundSpecial5(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special5();
    }

    @Override
    public boolean effect(int islandRef){
        if(special.getNoEntry() > 0){
            islandsManager.increaseInhibited(islandRef);
            special.decreaseNoEntry();
            noEntryListener.notifyNoEntry(special.getNoEntry());
            return true;
        }
        return false;
    }

    @Override
    public void setNoEntryListener(NoEntryListener noEntryListener){
        this.noEntryListener = noEntryListener;
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
    public int getNoEntry(){ return special.getNoEntry(); } //it's needed for restore

    @Override
    public void effect(){
        int inhibitedIsland = 0;
        for (int i = 0; i<islandsManager.size(); i++)
            if(islandsManager.getInhibited(i)>0) inhibitedIsland+=islandsManager.getInhibited(i);

        if(inhibitedIsland<(4-special.getNoEntry())){
            special.increaseNoEntry();
            noEntryListener.notifyNoEntry(special.getNoEntry());
        }
    }

    private class Special5 extends Special{
        private int noEntry;

        public Special5(){
            super(2, "special5");
            noEntry = 4;
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