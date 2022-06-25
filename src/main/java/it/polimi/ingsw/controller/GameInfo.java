package it.polimi.ingsw.controller;

import java.io.Serializable;

/**
 * GameInfo contains the game setting of this game. It is used to notify at first client connected the last saved game settings if he wants to restore last saved game.
 * It's not an inner class of VirtualView because server needs to use its.
 */
public class GameInfo implements Serializable {
    private final int numberOfPlayer;
    private final boolean expertMode;

    public GameInfo(int numberOfPlayer, boolean expertMode){
        this.numberOfPlayer = numberOfPlayer;
        this.expertMode = expertMode;
    }

    public int getNumberOfPlayer() { return numberOfPlayer; }
    public boolean isExpertMode() { return expertMode; }
}
