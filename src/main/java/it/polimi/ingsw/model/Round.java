package it.polimi.ingsw.model;

public class Round extends RoundStrategy{

    public Round(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){
        super(numberOfPlayer,playersInfo, cloudsManager, islandsManager, playerManager, bag);
    }

    @Override
    public int getCost(){
        return 0;
    }
    @Override
    public void increaseCost(){}

    @Override
    public String getName() {
        return null;
    }

}