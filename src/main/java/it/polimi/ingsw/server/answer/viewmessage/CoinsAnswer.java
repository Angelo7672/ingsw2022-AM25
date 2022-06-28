package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

/**
 * CoinsAnswer contains an update of a player's coins.
 */
public class CoinsAnswer implements Answer {
    private final int coin;
    private final int playerRef;

    public CoinsAnswer(int coin, int playerRef) {
        this.coin = coin;
        this.playerRef = playerRef;
    }

    public int getCoin() { return coin; }
    public int getPlayerRef() { return playerRef; }
}
