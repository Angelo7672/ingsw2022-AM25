package it.polimi.ingsw.controller.listeners;

public interface IslandSizeListener {
    public void notifyIslandSizeChange(int islandRef, int islandToDelete);
}
// notify the reference to the island which size increases by 1, and the reference to the island tile that will be unified (so
// it is removed from the islands' list)
