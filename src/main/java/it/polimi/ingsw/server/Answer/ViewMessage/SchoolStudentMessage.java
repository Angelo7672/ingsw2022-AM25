package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class SchoolStudentMessage implements Answer {
    String place; //entrance or table
    private final int color;
    private final int playerRef;
    private final int newValue;

    SchoolStudentMessage(int color, String place, int playerRef, int newValue){
        this.color = color;
        this.place = place;
        this.playerRef = playerRef;
        this.newValue = newValue;
    }
    @Override
    public String getMessage() {
        return place;
    }

    public int getColor() {
        return color;
    }

    public int getPlayerRef() {
        return playerRef;
    }

    public int getNewValue() {
        return newValue;
    }
}
