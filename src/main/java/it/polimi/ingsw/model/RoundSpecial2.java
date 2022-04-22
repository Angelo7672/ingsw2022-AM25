package it.polimi.ingsw.model;

public class RoundSpecial2 extends RoundStrategy {

    Special2 special;

    public RoundSpecial2(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer,playersInfo, cloudsManager, islandsManager, playerManager, bag);
        special = new Special2();
    }

    @Override
    public void moveStudent(int playerRef, int colour, boolean inSchool, int islandRef){
        if(!inSchool){
            playerManager.transferStudent(playerRef, colour, inSchool, true);
            islandsManager.incStudent(islandRef,colour);
        }
        else if(inSchool)
            playerManager.transferStudent(playerRef, colour, inSchool, true);
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

    private class Special2 extends Special{


        public Special2(){
            super(2, "special2");
        }

        //prende solo il controllo dei prof
        //non è da fare qua

    }


}