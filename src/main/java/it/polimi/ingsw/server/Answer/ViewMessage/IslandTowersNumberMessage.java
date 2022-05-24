package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class IslandTowersNumberMessage implements Answer {
    private final int islandRef;
    private final int towersNumber;

    public IslandTowersNumberMessage(int islandRef, int towersNumber, int towersColor) {
        this.islandRef = islandRef;
        this.towersNumber = towersNumber;
    }

    @Override
    public String getMessage() {
        return null;
    }

    public int getIslandRef() {
        return islandRef;
    }

    public int getTowersNumber() {
        return towersNumber;
    }
}
