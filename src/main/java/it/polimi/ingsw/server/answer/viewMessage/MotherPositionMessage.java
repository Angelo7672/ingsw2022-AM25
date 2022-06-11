package it.polimi.ingsw.server.answer.viewMessage;

import it.polimi.ingsw.server.answer.Answer;

public class MotherPositionMessage implements Answer {
    private final int motherPosition;

    public MotherPositionMessage(int motherPosition) {
        this.motherPosition = motherPosition;
    }

    public int getMotherPosition() { return motherPosition; }

}
