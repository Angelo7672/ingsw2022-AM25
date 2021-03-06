package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * IslandTowersColorAnswer contains info about the towers' color on an island.
 */
public class IslandTowersColorAnswer implements Answer {
    private final int islandRef;
    private final int color;

    /**
     * Create the island with a towers' color.
     * @param islandRef island reference;
     * @param color color reference;
     */
    public IslandTowersColorAnswer(int islandRef, int color) {
        this.islandRef = islandRef;
        this.color = color;
    }

    public int getColor() { return color; }
    public int getIslandRef() { return islandRef; }
}