package it.polimi.ingsw.server.Answer;

public class UnifiedIsland implements Answer{

    private final int unifiedIsland;

    public UnifiedIsland(int unifiedIsland) {
        this.unifiedIsland = unifiedIsland;
    }

    public int getUnifiedIsland() {
        return unifiedIsland;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
