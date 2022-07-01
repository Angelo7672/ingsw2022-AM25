package it.polimi.ingsw.server.answer.viewAnswer;

import it.polimi.ingsw.server.answer.Answer;

/**
 * UnifiedIslandAnswer contains info about the island that joined with another.
 */
public class UnifiedIslandAnswer implements Answer{
    private final int unifiedIsland;

    /**
     * Create an answer contains info about the island that joined with another.
     * @param unifiedIsland island that joined;
     */
    public UnifiedIslandAnswer(int unifiedIsland) {
        this.unifiedIsland = unifiedIsland;
    }

    public int getUnifiedIsland() {
        return unifiedIsland;
    }
}
