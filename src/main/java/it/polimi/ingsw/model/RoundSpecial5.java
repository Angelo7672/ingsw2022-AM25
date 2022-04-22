package it.polimi.ingsw.model;

public class RoundSpecial5 extends RoundStrategy{

    Special5 special;

    public RoundSpecial5(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer,playersInfo, cloudsManager, islandsManager, playerManager, bag);
        special = new Special5();
    }

    @Override
    public boolean moveMotherNature(int queueRef, int desiredMovement, int noColor, int islandRef) {
        int maxMovement;
        boolean victory = false;
        effect(islandRef);

        maxMovement = playerManager.readMaxMotherNatureMovement(queueRef);
        if (desiredMovement > 0 && desiredMovement <= maxMovement) {
            islandsManager.moveMotherNature(desiredMovement);
            if (islandsManager.getInhibited(islandsManager.getMotherPos())) {
                islandsManager.setInhibited(islandsManager.getMotherPos(), false);
            } else {
                victory = conquestIsland(islandsManager.getMotherPos(), noColor, queueRef);
            }
        }
        return victory;
    }

    public void effect(int islandRef){
        if(special.getNoEntry()>0){
            islandsManager.setInhibited(islandRef, true);
            special.decreaseNoEntry();
        }
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
    public void effect(){
        int inhibitedIsland = 0;
        for (int i = 0; i<islandsManager.size(); i++){
            if(islandsManager.getInhibited(i)) inhibitedIsland++;
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
