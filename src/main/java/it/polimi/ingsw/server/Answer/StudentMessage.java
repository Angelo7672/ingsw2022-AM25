package it.polimi.ingsw.server.Answer;

import java.util.ArrayList;

//clouds, island
public class StudentMessage implements Answer{
    private ArrayList<Object> group;
    private final int[] students;
    private final String message;

    public StudentMessage(ArrayList<Object> group, int[] students, String message) {
        this.students = students;
        this.message = message;
        this.group = group;
    }

    public int[] getStudents(){
        return students;
    }

    public void addGroup(ArrayList<Object> group){
        this.group = group;
    }
    public ArrayList<Object> getGroup(){
        return group;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
