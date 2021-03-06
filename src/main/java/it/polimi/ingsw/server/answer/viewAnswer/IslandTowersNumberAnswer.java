package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * IslandTowersNumberAnswer contains info about the towers' number on an island.
 */
public class IslandTowersNumberAnswer implements Answer {
    private final int islandRef;
    private final int towersNumber;

    /**
     * Create an island with a number of towers.
     * @param islandRef island reference;
     * @param towersNumber towers number;
     */
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
