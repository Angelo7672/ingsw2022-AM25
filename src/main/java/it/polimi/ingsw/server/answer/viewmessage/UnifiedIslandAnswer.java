package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class UnifiedIslandAnswer implements Answer{
    private final int unifiedIsland;

    public UnifiedIslandAnswer(int unifiedIsland) {
        this.unifiedIsland = unifiedIsland;
    }

    public int getUnifiedIsland() {
        return unifiedIsland;
    }

}
