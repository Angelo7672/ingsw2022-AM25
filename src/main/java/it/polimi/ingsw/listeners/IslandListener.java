package it.polimi.ingsw.listeners;

/**
 *  notify the reference to the island tile that needs to be deleted after it is unified to another (so it is removed from the islands' list)
 */
public interface IslandListener {
    public void notifyIslandChange(int islandToDelete);
}