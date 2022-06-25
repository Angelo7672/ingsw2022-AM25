package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class InhibitedIslandAnswer implements Answer {
    private final int islandRef;
    private final int Inhibited;

    public InhibitedIslandAnswer(int islandRef, int inhibited) {
        this.islandRef = islandRef;
        Inhibited = inhibited;
    }
    public int getIslandRef() {
        return islandRef;
    }
    public int getInhibited() {
        return Inhibited;
    }
}
