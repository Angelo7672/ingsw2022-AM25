package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class MotherPositionMessage implements Answer {
    private final boolean motherPosition;
    private final int islandRef;

    public MotherPositionMessage(boolean motherPosition, int islandRef) {
        this.motherPosition = motherPosition;
        this.islandRef = islandRef;
    }

    public boolean isMotherPosition() {
        return motherPosition;
    }

    public int getIslandRef() {
        return islandRef;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
