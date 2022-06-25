package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class IslandTowersNumberAnswer implements Answer {
    private final int islandRef;
    private final int towersNumber;

    public IslandTowersNumberAnswer(int islandRef, int towersNumber) {
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