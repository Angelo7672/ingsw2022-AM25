package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class SchoolStudentMessage implements Answer {
    String place; //entrance or table
    private final int color;
    private final int componentRef;
    private final int newValue;

    public SchoolStudentMessage(int color, String place, int componentRef, int newValue){
        this.color = color;
        this.place = place;
        this.componentRef = componentRef;
        this.newValue = newValue;
    }

    public String getMessage() {
        return place;
    }
    public int getColor() {
        return color;
    }
    public int getComponentRef() {
        return componentRef;
    }
    public int getNewValue() {
        return newValue;
    }
}
