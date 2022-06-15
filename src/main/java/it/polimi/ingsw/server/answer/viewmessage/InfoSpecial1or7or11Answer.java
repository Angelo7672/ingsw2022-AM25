package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

import java.util.ArrayList;

public class InfoSpecial1or7or11Answer implements Answer {
    private ArrayList<Integer> students;
    private final int specialIndex;

    public InfoSpecial1or7or11Answer(int specialIndex, ArrayList<Integer> students) {
        this.specialIndex = specialIndex;
        this.students = students;
    }

    public ArrayList<Integer> getStudents() { return students; }
    public int getSpecialIndex() { return specialIndex; }
}
