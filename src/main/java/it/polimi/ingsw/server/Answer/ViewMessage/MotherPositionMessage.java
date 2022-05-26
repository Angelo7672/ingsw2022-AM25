package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class MotherPositionMessage implements Answer {
    private final int motherPosition;

    public MotherPositionMessage(int motherPosition) {
        this.motherPosition = motherPosition;
    }

    public int getMotherPosition() {
        return motherPosition;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
