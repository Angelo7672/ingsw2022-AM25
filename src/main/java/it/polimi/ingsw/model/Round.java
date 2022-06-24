package it.polimi.ingsw.model;

public class Round extends RoundStrategy{

    public Round(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, QueueManager queueManager, Bag bag){
        super(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
    }

    @Override
    public int getCost(){
        return 0;
    }
    @Override
    public void increaseCost(){}
    @Override
    public void setCost(int cost){}
}