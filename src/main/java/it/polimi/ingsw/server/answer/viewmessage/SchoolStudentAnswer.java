package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class SchoolStudentAnswer implements Answer {
    String place; //entrance or table
    private final int color;
    private final int componentRef;
    private final int newValue;

    public SchoolStudentAnswer(int color, String place, int componentRef, int newValue){
        this.color = color;
        this.place = place;
        this.componentRef = componentRef;
        this.newValue = newValue;
    }

    public String getPlace() {
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
