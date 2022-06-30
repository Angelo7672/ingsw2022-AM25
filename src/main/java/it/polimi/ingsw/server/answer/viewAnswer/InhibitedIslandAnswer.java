package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * InhibitedIslandAnswer contains info about the number of noEntryCards on an island.
 */
public class InhibitedIslandAnswer implements Answer {
    private final int islandRef;
    private final int inhibited;

    /**
     * Number of noEntryCards on an island.
     * @param islandRef island reference;
     * @param inhibited number of noEntryCards;
     */
    public InhibitedIslandAnswer(int islandRef, int inhibited) {
        this.islandRef = islandRef;
        this.inhibited = inhibited;
    }
    public int getIslandRef() {
        return islandRef;
    }
    public int getInhibited() {
        return inhibited;
    }
}
