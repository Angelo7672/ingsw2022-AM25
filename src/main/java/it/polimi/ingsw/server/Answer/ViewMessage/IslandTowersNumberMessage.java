package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class IslandTowersNumberMessage implements Answer {
    private final int islandRef;
    private final int towersNumber;

    public IslandTowersNumberMessage(int islandRef, int towersNumber) {
        this.islandRef = islandRef;
        this.towersNumber = towersNumber;
    }

    public int getIslandRef() {
        return islandRef;
    }
    public int getTowersNumber() {
        return towersNumber;
    }
}
