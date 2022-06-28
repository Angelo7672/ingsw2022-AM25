package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

/**
 * IslandStudentAnswer contains info about the number of student of a color on an island.
 */
public class IslandStudentAnswer implements Answer {
    private final int islandRef;
    private final int color;
    private final int newValue;

    public IslandStudentAnswer(int islandRef, int color, int newValue) {
        this.islandRef = islandRef;
        this.color = color;
        this.newValue = newValue;
    }

    public int getColor() {
        return color;
    }
    public int getIslandRef() {
        return islandRef;
    }
    public int getNewValue() {
        return newValue;
    }
}
