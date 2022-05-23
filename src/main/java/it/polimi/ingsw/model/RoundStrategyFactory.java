package it.polimi.ingsw.model;


public class RoundStrategyFactory {
    int numberOfPlayer;
    CloudsManager cloudsManager;
    IslandsManager islandsManager;
    PlayerManager playerManager;
    QueueManager queueManager;
    Bag bag;

    RoundStrategyFactory(int numberOfPlayer, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager,QueueManager queueManager, Bag bag){
        this.numberOfPlayer = numberOfPlayer;
        this.cloudsManager = cloudsManager;
        this.islandsManager = islandsManager;
        this.playerManager = playerManager;
        this.queueManager = queueManager;
        this.bag = bag;
    }
    public RoundStrategy getRoundStrategy(int i){
        switch (i){
            case(1): return new RoundSpecial1(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case(2): return new RoundSpecial2(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case(3): return new RoundSpecial3(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case(4): return new RoundSpecial4(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case(5): return new RoundSpecial5(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case(6): return new RoundSpecial6(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case(7): return new RoundSpecial7(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case(8): return new RoundSpecial8(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case(9): return new RoundSpecial9(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case(10): return new RoundSpecial10(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case(11): return new RoundSpecial11(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case(12): return new RoundSpecial12(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            default: return null;
        }
    }
}
