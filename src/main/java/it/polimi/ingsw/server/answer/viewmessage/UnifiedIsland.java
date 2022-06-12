package it.polimi.ingsw.server.answer.viewmessage;

import it.polimi.ingsw.server.answer.Answer;

public class UnifiedIsland implements Answer{
    private final int unifiedIsland;

    public UnifiedIsland(int unifiedIsland) {
        this.unifiedIsland = unifiedIsland;
    }

    public int getUnifiedIsland() {
        return unifiedIsland;
    }

}
