package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class MotherPositionAnswer implements Answer {
    private final int motherPosition;

    public MotherPositionAnswer(int motherPosition) {
        this.motherPosition = motherPosition;
    }

    public int getMotherPosition() { return motherPosition; }

}
