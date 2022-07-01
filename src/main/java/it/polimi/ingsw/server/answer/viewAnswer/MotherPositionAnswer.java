package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * MotherPositionAnswer contains info about the mother nature position.
 */
public class MotherPositionAnswer implements Answer {
    private final int motherPosition;

    /**
     * Create an answer contains hte mother position.
     * @param motherPosition mother pose;
     */
    public MotherPositionAnswer(int motherPosition) {
        this.motherPosition = motherPosition;
    }

    public int getMotherPosition() { return motherPosition; }
}
