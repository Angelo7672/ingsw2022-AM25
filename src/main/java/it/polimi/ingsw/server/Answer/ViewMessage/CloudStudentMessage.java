package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class CloudStudentMessage implements Answer {
    private final int cloudRef;
    private final int color;
    private final int newValue;

    public CloudStudentMessage(int cloudRef, int color, int newValue) {
        this.cloudRef = cloudRef;
        this.color = color;
        this.newValue = newValue;
    }

    @Override
    public String getMessage() {
        return null;
    }

    public int getCloudRef() {
        return cloudRef;
    }

    public int getColor() {
        return color;
    }

    public int getNewValue() {
        return newValue;
    }
}
