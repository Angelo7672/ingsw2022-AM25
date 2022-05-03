package it.polimi.ingsw.server.Answer;

import java.util.ArrayList;

//clouds, island
public class StudentMessage implements Answer{
    private ArrayList<Object> group;
    private final int[] students;
    private final String message;

    public StudentMessage(String message) {
        this.students = null;
        this.message = message;
    }

    public StudentMessage(int[] students, String message) {
        this.students = students;
        this.message = message;
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
    public Object getMessage() {
        return message;
    }
}
