package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class SetSpecialAnswer implements Answer{
    private final int specialRef;

    public SetSpecialAnswer(int specialRef){
        this.specialRef = specialRef;
    }

    @Override
    public String getMessage() {
        return null;
    }
    public int getSpecialRef() { return specialRef; }
}
