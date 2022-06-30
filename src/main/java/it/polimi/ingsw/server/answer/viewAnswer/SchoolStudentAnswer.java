package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * SchoolStudentAnswer contains info about a color's students number in a school.
 */
public class SchoolStudentAnswer implements Answer {
    String place; //entrance or table
    private final int color;
    private final int playerRef;
    private final int newValue;

    public SchoolStudentAnswer(int color, String place, int playerRef, int newValue){
        this.color = color;
        this.place = place;
        this.playerRef = playerRef;
        this.newValue = newValue;
    }

    public String getPlace() {
        return place;
    }
    public int getColor() {
        return color;
    }
    public int getComponentRef() {
        return playerRef;
    }
    public int getNewValue() {
        return newValue;
    }
}
