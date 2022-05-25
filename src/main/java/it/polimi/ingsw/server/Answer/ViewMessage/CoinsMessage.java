package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class CoinsMessage implements Answer {
    private final int coin;
    private final int playerRef;

    public CoinsMessage(int coin, int playerRef) {
        this.coin = coin;
        this.playerRef = playerRef;
    }

    public int getCoin() {
        return coin;
    }

    public int getPlayerRef() {
        return playerRef;
    }

    @Override
    public String getMessage() {
        return null;
    }
}