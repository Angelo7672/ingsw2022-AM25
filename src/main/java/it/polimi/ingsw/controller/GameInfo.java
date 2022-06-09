package it.polimi.ingsw.controller;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private int numberOfPlayer;
    private boolean expertMode;

    public GameInfo(int numberOfPlayer, boolean expertMode){
        this.numberOfPlayer = numberOfPlayer;
        this.expertMode = expertMode;
    }

    public int getNumberOfPlayer() { return numberOfPlayer; }
    public boolean isExpertMode() { return expertMode; }
}
