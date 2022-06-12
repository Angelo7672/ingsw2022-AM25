package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class SetSpecialAnswer implements Answer{
    private final int specialRef;

    public SetSpecialAnswer(int specialRef){
        this.specialRef = specialRef;
    }

    public int getSpecialRef() { return specialRef; }
}
