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

    /**
     * Create an answer contains info about a color's students number in a school.
     * @param color color reference;
     * @param place where the student should be placed;
     * @param playerRef player reference;
     * @param newValue numbers of students;
     */
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
