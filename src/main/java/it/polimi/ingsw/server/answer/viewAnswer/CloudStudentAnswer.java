package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * CloudStudentAnswer contains info about an update of students on a cloud.
 */
public class CloudStudentAnswer implements Answer {
    private final int cloudRef;
    private final int color;
    private final int newValue;

    public CloudStudentAnswer(int cloudRef, int color, int newValue) {
        this.cloudRef = cloudRef;
        this.color = color;
        this.newValue = newValue;
    }

    public int getCloudRef() { return cloudRef; }
    public int getColor() {  return color; }
    public int getNewValue() { return newValue; }
}
