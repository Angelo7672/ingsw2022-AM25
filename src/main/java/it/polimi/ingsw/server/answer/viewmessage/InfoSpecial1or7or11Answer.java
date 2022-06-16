package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

import java.util.ArrayList;

public class InfoSpecial1or7or11Answer implements Answer {
    private final int studentColor;
    private final boolean addOrRemove;
    private final int specialIndex;

    public InfoSpecial1or7or11Answer(int specialIndex, int studentColor, boolean addOrRemove) {
        this.specialIndex = specialIndex;
        this.studentColor = studentColor;
        this.addOrRemove = addOrRemove;
    }

    public int getStudentColor() { return studentColor; }
    public int getSpecialIndex() { return specialIndex; }
    public boolean isAddOrRemove() { return addOrRemove; }
}
