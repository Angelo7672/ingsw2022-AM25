package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class SetSpecialAnswer implements Answer{
    private final int specialRef;
    private final int cost;

    public SetSpecialAnswer(int specialRef,int cost){
        this.specialRef = specialRef;
        this.cost = cost;
    }

    public int getSpecialRef() { return specialRef; }
}
