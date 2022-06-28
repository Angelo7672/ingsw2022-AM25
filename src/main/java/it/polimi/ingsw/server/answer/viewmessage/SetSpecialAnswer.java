package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

/**
 * SetSpecialAnswer contains info about the cost of a special.
 */
public class SetSpecialAnswer implements Answer{
    private final int specialRef;
    private final int cost;

    public SetSpecialAnswer(int specialRef,int cost){
        this.specialRef = specialRef;
        this.cost = cost;
    }

    public int getSpecialRef() { return specialRef; }
    public int getCost() {
        return cost;
    }
}
