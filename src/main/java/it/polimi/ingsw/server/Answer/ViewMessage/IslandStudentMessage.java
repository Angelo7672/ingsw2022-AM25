package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class IslandStudentMessage implements Answer {
    private final int islandRef;
    private final int color;
    private final int newValue;

    public IslandStudentMessage(int islandRef, int color, int newValue) {
        this.islandRef = islandRef;
        this.color = color;
        this.newValue = newValue;
    }

    @Override
    public String getMessage() {
        return null;
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
