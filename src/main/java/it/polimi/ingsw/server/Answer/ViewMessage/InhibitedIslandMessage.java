package it.polimi.ingsw.server.Answer.ViewMessage;

import it.polimi.ingsw.server.Answer.Answer;

public class InhibitedIslandMessage implements Answer {
    private final int islandRef;
    private final int Inhibited;

    public InhibitedIslandMessage(int islandRef, int inhibited) {
        this.islandRef = islandRef;
        Inhibited = inhibited;
    }

    @Override
    public String getMessage() {
        return null;
    }
    public int getIslandRef() {
        return islandRef;
    }
    public int getInhibited() {
        return Inhibited;
    }
}
