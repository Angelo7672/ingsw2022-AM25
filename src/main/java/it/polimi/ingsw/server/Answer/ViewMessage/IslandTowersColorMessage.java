package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class IslandTowersColorMessage implements Answer {
    private final int islandRef;
    private final int color;

    public IslandTowersColorMessage(int islandRef, int color) {
        this.islandRef = islandRef;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getIslandRef() {
        return islandRef;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
