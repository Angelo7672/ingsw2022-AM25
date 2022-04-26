package it.polimi.ingsw.model;


public class RoundStrategyFactory {
    int numberOfPlayer;
    String[] playersInfo;
    CloudsManager cloudsManager;
    IslandsManager islandsManager;
    PlayerManager playerManager;
    Bag bag;

    RoundStrategyFactory(int numberOfPlayer, String[] playersInfo, CloudsManager cloudsManager, IslandsManager islandsManager,PlayerManager playerManager, Bag bag){

        this.numberOfPlayer = numberOfPlayer;
        this.playersInfo = playersInfo;
        this.cloudsManager = cloudsManager;
        this.islandsManager = islandsManager;
        this.playerManager = playerManager;
        this.bag = bag;
    }
    public RoundStrategy getRoundStrategy(int i){
        switch (i){
            case(1): return new RoundSpecial1(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            case(2): return new RoundSpecial2(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            case(3): return new RoundSpecial3(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            case(4): return new RoundSpecial4(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            case(5): return new RoundSpecial5(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            case(6): return new RoundSpecial6(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            case(7): return new RoundSpecial7(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            case(8): return new RoundSpecial8(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            case(9): return new RoundSpecial9(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            case(10): return new RoundSpecial10(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            case(11): return new RoundSpecial11(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            case(12): return new RoundSpecial12(numberOfPlayer, playersInfo, cloudsManager, islandsManager, playerManager, bag);
            default: return null;
        }
    }
}
