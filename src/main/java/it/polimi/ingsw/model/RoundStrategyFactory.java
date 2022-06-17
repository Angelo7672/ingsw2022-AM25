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
        return switch (i) {
            case (1) -> new RoundSpecial1(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case (2) -> new RoundSpecial2(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case (3) -> new RoundSpecial3(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case (4) -> new RoundSpecial4(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case (5) -> new RoundSpecial5(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case (6) -> new RoundSpecial6(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case (7) -> new RoundSpecial7(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case (8) -> new RoundSpecial8(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case (9) -> new RoundSpecial9(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case (10) -> new RoundSpecial10(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case (11) -> new RoundSpecial11(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            case (12) -> new RoundSpecial12(numberOfPlayer, cloudsManager, islandsManager, playerManager, queueManager, bag);
            default -> null;
        };
    }
}
