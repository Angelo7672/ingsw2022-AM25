package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * InfoSpecial1or7or11Answer contains info about a color's number of students on special1, special7 or special11.
 */
public class InfoSpecial1or7or11Answer implements Answer {
    private final int studentColor;
    private final int value;
    private final int specialIndex;

    public InfoSpecial1or7or11Answer(int specialIndex, int studentColor, int value) {
        this.specialIndex = specialIndex;
        this.studentColor = studentColor;
        this.value = value;
    }

    public int getStudentColor() { return studentColor; }
    public int getSpecialIndex() { return specialIndex; }
    public int getValue() { return value; }
}
