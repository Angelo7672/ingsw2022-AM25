package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.NoEntryListener;

/**
 * Round strategy used by special 5.
 */
public class RoundSpecial5 extends RoundStrategy{
    private final Special5 special;
    protected NoEntryListener noEntryListener;

    public RoundSpecial5(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
        special = new Special5();
    }

    /**
     * It checks if effect could be used, then use it.
     * @param islandRef is the island's number.
     * @return true if effect is used.
     */
    @Override
    public boolean effect(int islandRef){
        if(special.getNoEntry() > 0 && islandRef>=0 && islandsManager.getIslandsSize()>islandRef){
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
    public void noEntryCardsRestore(int numCards){
        special.restoreNoEntry(numCards);
        noEntryListener.notifyNoEntry(special.getNoEntry());
    }
    @Override
    public void initializeSpecial(){
        special.initializeSpecial();
        noEntryListener.notifyNoEntry(special.getNoEntry());
    }
    @Override
    public void setCost(int cost){ special.setCost(cost); }
    @Override
    public int getCost(){
        return special.getCost();
    }
    @Override
    public void increaseCost(){ special.increaseCost(); }

    /**
     * It counts the number of island inhibited and compare it to the no entry number. If one no entry is missing, it increases the number of the tiles.
     */
    @Override
    public void effect(){
        int inhibitedIsland = 0;
        for (int i = 0; i<islandsManager.getIslandsSize(); i++)
            if(islandsManager.getInhibited(i)>0) inhibitedIsland+=islandsManager.getInhibited(i);

        if(inhibitedIsland<(4-special.getNoEntry())){
            special.increaseNoEntry();
            noEntryListener.notifyNoEntry(special.getNoEntry());
        }
    }

    private static class Special5 extends Special{
        private int noEntry;

        public Special5(){
            super(2);
        }

        private void initializeSpecial(){ noEntry = 4; }
        public int getNoEntry(){
            return noEntry;
        }
        public void increaseNoEntry(){
            noEntry+=1;
        }
        public void decreaseNoEntry(){
            noEntry-=1;
        }
        private void restoreNoEntry(int numCards){ noEntry = numCards; }
    }
}