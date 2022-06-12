package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

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

}
